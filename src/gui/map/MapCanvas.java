package gui.map;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import model.Airport;
import util.Dialogs;

public class MapCanvas extends Canvas {

    private List<Airport> airports = new ArrayList<>();//svi podaci

    private final Set<String> visible = new HashSet<>();//kolekcija koja cuva jedinstvene elemente, cuvanje vidljivih

    // Selekcija i treperenje
    private volatile String selectedCode = null;//promena iz jedne niti je vidljiva drugim
    private volatile boolean blinkOn = false;
    private volatile boolean blinking = false;
    private Thread blinkThread;

    private final Runnable onSelectStart;//sta da radi timer
    private final Runnable onSelectEnd;

    private static final int SQUARE = 12;//dimenzija kvadratica
    private static final int HALF = SQUARE / 2;//centriranje kvadrata oko koordinate
    private static final int LABEL_DX = 8;//koliko da se Code pomeri od kvadrata
    private static final int LABEL_DY = -6;

    private double scale = 2.0;//faktor uvecanja

    public MapCanvas(Runnable onSelectStart, Runnable onSelectEnd) {
        this.onSelectStart =  onSelectStart;
        this.onSelectEnd   =  onSelectEnd;
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                try {
                    String hit = findAirportAt(e.getX(), e.getY());//vraca kod aerodroma koji je kliknut
                    if (hit == null) {
                        if (selectedCode != null) clearSelection();//ako je kliknuto bilo koje polje drugo na canvas-u
                        return;
                    }
                    setSelection(hit);
                } catch (Exception ex) {
                    Dialogs.showError(getFrameOwner(), "Greška pri selekciji: " + ex.getMessage());
                }
            }
        });
    }


    public void setAirports(List<Airport> list) {//ubacuje aerdrome iz DataAirports
        this.airports = (list != null) ? new ArrayList<>(list) : new ArrayList<>();
        repaint();
    }
    public boolean isVisible(String code) {
        return visible.contains(code);
    }

    public void setAirportVisible(String code, boolean v) {
    	if (code == null) return;
        if (v) visible.add(code);
        else   visible.remove(code);
        if (selectedCode != null && !visible.contains(selectedCode)) {
            clearSelection();//ako je prethodno postojao selektovani
        }
       repaint();
    }

    @Override
    public void update(Graphics g) {
    	paint(g); // ne brisi pozadinu pre paint
    }
    @Override
    public void paint(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // iscrtavanje aerodroma
        for (Airport a : airports) {
            String code = a.getCode();
            if (!isVisible(code)) continue;

            Point p = getPointInCanvas(a.getX(), a.getY());
            boolean isSelected = code.equals(selectedCode);

            Color fill = (isSelected && blinkOn) ? Color.RED : Color.GRAY;
            g.setColor(fill);
            g.fillRect(p.x - HALF, p.y - HALF, SQUARE, SQUARE);

            g.setColor(Color.DARK_GRAY);
            g.drawRect(p.x - HALF, p.y - HALF, SQUARE, SQUARE);

            g.setColor(isSelected && blinkOn ? Color.RED : Color.BLACK);
            g.drawString(code, p.x + LABEL_DX, p.y + LABEL_DY);
        }
    }

    private void startBlink() {
        if (blinking) return;//jedna blink nit
        blinking = true;
        blinkThread = new Thread(() -> {
            try {
                while (blinking) {
                    blinkOn = !blinkOn;//naizmenicno boji crveno/crno u paint
                    Rectangle r = getAirportBounds(selectedCode);
                    if (r != null) repaint(r.x, r.y, r.width, r.height);//iscrtavamo opet samo selektovani aerodrom
                    Thread.sleep(350);
                }
            } catch (InterruptedException ie) {
            } finally {
                blinkOn = false;
                repaint();
            }
        });
        blinkThread.setDaemon(true);//gasenjem app demonske niti se automatski prekidaju
        blinkThread.start();
    }

    private void stopBlink() {
        blinking = false;
        if (blinkThread != null) blinkThread.interrupt();
        blinkThread = null;
        blinkOn = false;
        repaint();
    }

    private void setSelection(String code) {
        if (Objects.equals(selectedCode, code)) {//ako je kod isti gasi
            clearSelection();
            return;
        }
        selectedCode = code;
        onSelectStart.run();//timer.pause()
        startBlink();
        repaint();
    }

    private void clearSelection() {
        selectedCode = null;
        stopBlink();
        onSelectEnd.run();//timer.go()
        repaint();
    }



    private String findAirportAt(int mx, int my) {//koordinate koje su kliknute
        for (Airport a : airports) {
            if (!isVisible(a.getCode())) continue;
            Point p = getPointInCanvas(a.getX(), a.getY());
            Rectangle r = new Rectangle(p.x - HALF, p.y - HALF, SQUARE, SQUARE);
            if (r.contains(mx, my)) return a.getCode();//ako je kliknuto u kvadrat vraca kod aerodroma
        }
        return null;
    }
    
    private Point getPointInCanvas(int ax, int ay) {//koordinata (0, 0)je inicijalno u gornjem levom uglu
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int sx = (int) Math.round(centerX + ax * scale);
        int sy = (int) Math.round(centerY - ay * scale); 
        return new Point(sx, sy);
    }
    private Rectangle getAirportBounds(String code) {
        if (code == null) return null;
        for (Airport a : airports) {
            if (code.equals(a.getCode())) {
                Point p = getPointInCanvas(a.getX(), a.getY());
                return new Rectangle(
                    p.x - HALF - 1, 
                    p.y - HALF - 1, 
                    SQUARE + 2, 
                    SQUARE + 2
                );
            }
        }
        return null;
    }


    private Frame getFrameOwner() {
        Container c = getParent();
        while (c != null && !(c instanceof Frame)) c = c.getParent();
        return (Frame) c;
    }
}
