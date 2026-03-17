package validation;

public final class Validators {
    private Validators() {}

    public static String checkField(String s, String field) throws ValidationException {
        if (s == null || s.trim().isEmpty())
            throw new ValidationException(field + " je obavezno polje.");
        return s.trim();//brise beline sa kraja i pocetka ucitanog stringa
    }

    public static int inRange(int v, int low, int high, String field) throws ValidationException {
        if (v < low || v > high) throw new ValidationException(field + " mora biti u opsegu ["+low+","+high+"].");
        return v;
    }
    public static int parseIntField(String s, String field) throws ValidationException {
        checkField(s, field);
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException nfe) {
            throw new ValidationException(field + " mora biti ceo broj.");
        }
    }
}

