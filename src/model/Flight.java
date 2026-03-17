package model;

import java.time.LocalTime;

import validation.ValidationException;
import validation.ValidatorForFlights;

public class Flight {

	private Airport from;
	private Airport to;
	private LocalTime departure;
	private int durationMin;
	
    public Flight(Airport from, Airport to, LocalTime departure, int durationMin) throws ValidationException {
        this.from = ValidatorForFlights.validFrom(from);
        this.to = ValidatorForFlights.validTo(to, from);
        this.departure = departure;//validacija odradjena pri dodavanju u MainFrame-u
        this.durationMin = ValidatorForFlights.validDuration(durationMin);
    }

	public Airport getFrom() {
		return from;
	}

	public void setFrom(Airport from) {
		this.from = from;
	}

	public Airport getTo() {
		return to;
	}

	public void setToCode(Airport to) {
		this.to = to;
	}

	public LocalTime getDeparture() {
		return departure;
	}

	public void setDeparture(LocalTime departure) {
		this.departure = departure;
	}

	public int getDurationMin() {
		return durationMin;
	}

	public void setDurationMin(int durationMin) {
		this.durationMin = durationMin;
	}
	
    @Override 
    public String toString() {
        return from.getCode()+"→"+to.getCode()+" "+departure+" ("+durationMin+" min)";
    }
}
