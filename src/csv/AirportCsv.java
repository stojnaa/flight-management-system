package csv;

import datastorage.DataAirports;
import model.Airport;
import validation.ValidationException;

import java.io.*;

public class AirportCsv implements BaseCsv<DataAirports> {

	@Override
	public void save(File file, DataAirports airports) throws Exception {
		try (PrintWriter out = new PrintWriter(new FileWriter(file))){//klasa za pisanje u fajl
			for (Airport a : airports) {
				out.printf("%s,%s,%d,%d%n", a.getName(), a.getCode(), a.getX(), a.getY());
			}
		}
	}
	@Override
	public void load(File file, DataAirports airports) throws Exception {
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
	                throw new ValidationException("Red " + row + " ima neispravan format aerodroma.");
	            }

	            try {
	                airports.addAirport(
	                    t[0].trim(),
	                    t[1].trim(),
	                    Integer.parseInt(t[2].trim()),
	                    Integer.parseInt(t[3].trim())
	                );
	            } catch (NumberFormatException nfe) {
	                throw new ValidationException("Red " + row + ": koordinate moraju biti celi brojevi.");
	            }

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
