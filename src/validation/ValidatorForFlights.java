package validation;

import java.time.LocalTime;
import model.Airport;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidatorForFlights {
	
	private static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");//format vremena za letove
	
	private ValidatorForFlights() {}
	
	public static Airport validFrom(Airport a) throws ValidationException {
        if (a == null) throw new ValidationException("Početni aerodrom je obavezan.");
        return a;
    }
	
	public static Airport validTo(Airport to, Airport from) throws ValidationException {
        if (to == null) throw new ValidationException("Krajnji aerodrom je obavezan.");
        if (from != null && to.equals(from)) throw new ValidationException("Pocetni i krajnji aerodrom ne mogu biti isti.");
        return to;
    }
	public static LocalTime validTime(String s) throws ValidationException {
	    if (s == null || s.trim().isEmpty())
	        throw new ValidationException("Vreme poletanja (HH:mm) je obavezno.");
	    try {
	        return LocalTime.parse(s.trim(), HHMM);
	    } catch (DateTimeParseException e) {
	        throw new ValidationException("Vreme mora biti u formatu HH:mm.");
	    }
	}


	public static int validDuration(int min) throws ValidationException {
        return Validators.inRange(min, 1, 24*60, "Trajanje (min)");//jedan dan
    }

}
