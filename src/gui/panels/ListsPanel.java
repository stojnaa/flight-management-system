package gui.panels;

import java.awt.*;

public class ListsPanel extends Panel {
    private final TextArea taAirports = new TextArea("", 10, 100, TextArea.SCROLLBARS_VERTICAL_ONLY);
    private final TextArea taFlights  = new TextArea("", 10, 100, TextArea.SCROLLBARS_VERTICAL_ONLY);

    public ListsPanel() {
        super(new GridLayout(1, 2));
        taAirports.setEditable(false);
        taFlights.setEditable(false);
        add(taAirports);
        add(taFlights);
    }

    public TextArea getTaAirports() { return taAirports; }
    public TextArea getTaFlights()  { return taFlights; }
}
