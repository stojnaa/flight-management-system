package validation;

public class ValidatorForAirports {
	
	private ValidatorForAirports() {}
	
	public static String notBlank(String s, String field) throws ValidationException {
        return Validators.checkField(s, field);
    }
	
    public static String validCode(String code) throws ValidationException {
        String c = Validators.checkField(code, "Kod");
        if (!c.matches("[A-Z]{3}"))
            throw new ValidationException("Kod mora biti troslovni (3 velika slova).");
        return c;
    }
	
	public static int validCoordinate(int coord) throws ValidationException {
        return Validators.inRange(coord, -90, 90, "Koordinata");
    }

}
