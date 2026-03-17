package datastorage;

import model.Airport;
import model.Flight;
import validation.ValidationException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataFlights implements Iterable<Flight> {

    private final DataAirports airports;
    private final List<Flight> flights = new ArrayList<>();

    public DataFlights(DataAirports airports) {
        this.airports = airports;
    }

    public Flight addFlight(Airport from, Airport to, LocalTime dep, int durationMin) throws ValidationException {
        Flight f = new Flight(from, to, dep, durationMin);
        flights.add(f);
        return f;
    }
  

    public int getFlightCount() {
        return flights.size();
    }

    public void clear() {
        flights.clear();
    }
    public Airport getAirport(String code) throws ValidationException {
        Airport a = airports.findByCode(code);
        if (a == null) throw new ValidationException("Aerodrom sa kodom '" + code + "' ne postoji.");
        return a;
    }
    public List<Flight> getList() {
        return flights;
    }

    @Override
    public Iterator<Flight> iterator() {
        return flights.iterator();
    }

}
