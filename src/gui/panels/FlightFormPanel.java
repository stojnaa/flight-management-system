package gui.panels;

import java.awt.*;

public class FlightFormPanel extends Panel {

    private final Choice chFrom = new Choice() {
        @Override public Dimension getPreferredSize() { return new Dimension(120, 25); }
    };
    private final Choice chTo = new Choice() {
        @Override public Dimension getPreferredSize() { return new Dimension(120, 25); }
    };
    private final TextField tfDep = new TextField(10);
    private final TextField tfDur = new TextField(10);
    private final Button btnAddFlight = new Button("Dodaj let");

    public FlightFormPanel() {
        super(new GridLayout(2, 1));

        Panel row1 = new Panel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        row1.add(new Label("Od:"));              row1.add(chFrom);
        row1.add(new Label("Do:"));              row1.add(chTo);
        row1.add(new Label("Polazak (HH:mm):")); row1.add(tfDep);

        Panel row2 = new Panel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        row2.add(new Label("Trajanje (min):"));  row2.add(tfDur);
        row2.add(btnAddFlight);

        add(row1);
        add(row2);
    }

    public Choice getChFrom() { return chFrom; }
    public Choice getChTo() { return chTo; }
    public TextField getTfDep() { return tfDep; }
    public TextField getTfDur() { return tfDur; }
    public Button getBtnAddFlight() { return btnAddFlight; }
    public void clearFlightInputs() {
        if (chFrom.getItemCount() > 0) chFrom.select(0); // prvi element 
        if (chTo.getItemCount() > 0)   chTo.select(0);   
        tfDep.setText("");  
        tfDur.setText("");  
    }
}

