package gui.panels;

import java.awt.*;

public class AirportFormPanel extends Panel {
    private final TextField tfName = new TextField(20);
    private final TextField tfCode = new TextField(5);
    private final TextField tfX = new TextField(4);
    private final TextField tfY = new TextField(4);
    private final Button btnAddAirport = new Button("Dodaj aerodrom");

    public AirportFormPanel() {
        super(new FlowLayout(FlowLayout.LEFT));
        add(new Label("Naziv:")); add(tfName);
        add(new Label("Kod:"));   add(tfCode);
        add(new Label("x:"));     add(tfX);
        add(new Label("y:"));     add(tfY);
        add(btnAddAirport);
    }


    public TextField getTfName() { return tfName; }
    public TextField getTfCode() { return tfCode; }
    public TextField getTfX() { return tfX; }
    public TextField getTfY() { return tfY; }
    public Button getBtnAddAirport() { return btnAddAirport; }
    public void clearAirportInputs() { 
    	tfName.setText(""); 
    	tfCode.setText(""); 
    	tfX.setText(""); 
    	tfY.setText(""); 
    }
}
