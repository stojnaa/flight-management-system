package model;

import validation.ValidationException;
import validation.ValidatorForAirports;

public class Airport {
	
	private String code;
	private String name;
	private int x;
	private int y;
	
    public Airport(String name, String code, int x, int y) throws ValidationException {
        this.name = ValidatorForAirports.notBlank(name, "Naziv");
        this.code = ValidatorForAirports.validCode(code);
        this.x = ValidatorForAirports.validCoordinate(x);
        this.y = ValidatorForAirports.validCoordinate(y);
    }
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
    @Override 
    public String toString() {
        return name + " (" + code + ") [" + x + "," + y + "]";
    }
    
    
    
    // dva aerodroma se smatraju jednakim ako imaju isti troslovni kod
	@Override 
	public boolean equals(Object o) {
	    if (!(o instanceof Airport)) return false;//ako nije objekat tipa Airport
		if (this == o) return true;
		Airport a = (Airport) o;//kastovanje na Airport
		return this.code.equals(a.code);
	}
	@Override 
	public int hashCode() { 
		return code.hashCode();//jedinstveni po code
	}
	

}
