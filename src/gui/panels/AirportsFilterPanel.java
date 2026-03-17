package gui.panels;

import java.awt.*;
import java.awt.event.*;
import model.Airport;
import gui.map.MapCanvas;
import datastorage.DataAirports;

public class AirportsFilterPanel extends Panel {

    private final Panel content = new Panel(new GridLayout(0, 1, 0, 6));//rows cols razmak izmedju jih
    private final ScrollPane scroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);

    public AirportsFilterPanel() {
        super(new BorderLayout());
        content.setBackground(new Color(245, 245, 245));
        scroll.add(content);
        scroll.setPreferredSize(new Dimension(260, 140)); // širina desnog stubca
        add(scroll, BorderLayout.CENTER);
    }

    public void rebuild(DataAirports airports, MapCanvas mapCanvas) {
        content.removeAll();

        for (Airport a : airports) {
            Panel row = new Panel(new BorderLayout());
            boolean initial = mapCanvas.isVisible(a.getCode()); // čita iz HashSet-a

            Checkbox cb = new Checkbox("", initial);
            cb.addItemListener(e ->
                mapCanvas.setAirportVisible(a.getCode(), cb.getState())
            );

            String text = a.getName() + " (" + a.getCode() + ") [" + a.getX() + "," + a.getY() + "]";
            row.add(cb, BorderLayout.WEST);
            row.add(new Label(text), BorderLayout.CENTER);

            content.add(row);
        }

        content.revalidate();
        content.repaint();
    }
}

