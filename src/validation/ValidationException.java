package validation;

// klasa koja predstavlja prilagodjene izuzetke nastale prilikom validacije podataka
public class ValidationException extends Exception {
    public ValidationException(String message) { 
        super(message); 
    }
}
