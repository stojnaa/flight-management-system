package datastorage;


import model.Airport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import validation.ValidationException;
import validation.ValidatorForAirports;

public class DataAirports implements Iterable <Airport> {//interfejs za prolazak kroz kolekciju
	

    private final List<Airport> airports = new ArrayList<>();

    public Airport addAirport(String name, String code, int x, int y) throws ValidationException {
        String c = ValidatorForAirports.validCode(code);
        checkDuplicate(c);

        Airport a = new Airport(name, c, x, y);
        airports.add(a);
        return a;
    }
    private void checkDuplicate(String code) throws ValidationException {
        for (Airport a : airports) {
            if (a.getCode().equals(code)) {
                throw new ValidationException("Aerodrom sa kodom " + code + " već postoji.");
            }
        }
    }

    public Airport findByCode(String code) {
        for (Airport a : airports) {
            if (a.getCode().equals(code)) {
                return a;
            }
        }
        return null;
    }
    public int getAirportCount() {
        return airports.size();
    }

    public void clear() {
        airports.clear();
    }

    public List<Airport> getList() {
        return airports;
    }
    

    @Override
    public Iterator<Airport> iterator() {
        return airports.iterator();
    }
}
