package csv;

import datastorage.DataAirports;
import datastorage.DataFlights;
import model.Flight;
import validation.ValidationException;
import model.Airport;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class FlightCsv implements BaseCsv<DataFlights> {
    private static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");

	@Override
	public void save(File file, DataFlights flights) throws Exception {
		try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (Flight f : flights) {
                out.printf("%s,%s,%s,%d%n",
                        f.getFrom().getCode(),
                        f.getTo().getCode(),
                        HHMM.format(f.getDeparture()),
                        f.getDurationMin()
                );
            }
        }
	}
	@Override
	public void load(File file, DataFlights flights) throws Exception {
	    if (file == null) {
	        throw new ValidationException("Fajl nije prosledjen.");
	    }
	    if (!file.exists()) {
	        throw new ValidationException("Fajl ne postoji.");
	    }

	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	        String line = br.readLine();
	        if (line == null) {
	            throw new ValidationException("Fajl " + file.getName() + " je prazan.");
	        }

	        int row = 1;
	        do {
	            if (line.trim().isEmpty()) {
	                line = br.readLine();
	                row++;
	                continue;
	            }

	            String[] t = line.split(",");
	            if (t.length != 4) {
	                throw new ValidationException("Red " + row + " ima neispravan format letova.");
	            }
	            String fromCode = t[0].trim();
                String toCode   = t[1].trim();
                String depStr   = t[2].trim();
                String durStr   = t[3].trim();

                if (!fromCode.matches("[A-Z]{3}") || !toCode.matches("[A-Z]{3}")) {
                    throw new ValidationException("Red " + row + ": From/To moraju biti tačno 3 VELIKA slova (A-Z).");
                }
                if (fromCode.equals(toCode)) {
                    throw new ValidationException("Red " + row + ": Polazni i odredisni aerodrom ne mogu biti isti.");
                }

                Airport from = flights.getAirport(fromCode);
                Airport to   = flights.getAirport(toCode);
                if (from == null ) {
                    throw new ValidationException("Red " + row + ": Nepoznat aerodrom sa kodom " + fromCode);
                }
                if (to == null) {
                    throw new ValidationException("Red " + row + ": Nepoznat aerodrom sa kodom  " + toCode);
                }
                LocalTime dep;
                try {
                    dep = LocalTime.parse(depStr, HHMM);
                } catch (DateTimeParseException dtpe) {
                    throw new ValidationException("Red " + row + ": Polazak mora biti u formatu HH:mm.");
                }

                int dur;
                try {
                    dur = Integer.parseInt(durStr);
                } catch (NumberFormatException nfe) {
                    throw new ValidationException("Red " + row + ": Trajanje mora biti ceo broj.");
                }
                if (dur <= 0 || dur > 1440) {
                    throw new ValidationException("Red " + row + ": Trajanje mora biti u opsegu 1–1440.");
                }
                flights.addFlight(from, to, dep, dur);
	         
	            line = br.readLine();
	            row++;
	        } while (line != null);
	    } catch (FileNotFoundException fnf) {
	        throw new ValidationException("Fajl ne moze da se otvori.");
	    } catch (IOException ioe) {
	        throw new ValidationException("Greška pri čitanju fajla.");
	    }
	}

}
