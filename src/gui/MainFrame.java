package gui;

import datastorage.*;
import gui.map.MapCanvas;
import model.*;
import util.*;
import gui.panels.*;
import gui.menus.*;
import validation.*;
import csv.*;
import java.awt.*; 
import java.awt.event.*; 
import java.io.File; 
import java.time.LocalTime; 


public class MainFrame extends Frame implements ActionListener {//osluskivac akcija
	private DataAirports airports = new DataAirports();
	private DataFlights flights = new DataFlights(airports);
	
	//paneli
    private final AirportFormPanel airportForm = new AirportFormPanel();
    private final FlightFormPanel  flightForm  = new FlightFormPanel();
    private final ListsPanel       listsPanel  = new ListsPanel();
	
    
    
 // CSV objekti
    private final AirportCsv airportCsv = new AirportCsv();
    private final FlightCsv  flightCsv  = new FlightCsv();
    
    //appTimer
    private Label labelTimer = new Label("00", Label.RIGHT);
    private Timer timer;

 // SOUTH: mapa + checkbox lista
    private MapCanvas mapCanvas;
    private final AirportsFilterPanel airportsFilter = new AirportsFilterPanel();


    
    public MainFrame() {
    	super("Realizacija aerodroma");
    	setLayout(new BorderLayout());//ivice i centar
    	add(buildTop(), BorderLayout.NORTH);//unos infomracija
        add(listsPanel, BorderLayout.CENTER);//tabelarni prikaz
        add(buildMap(), BorderLayout.SOUTH);//dugmad
        
        timer = new Timer(labelTimer, this);
        timer.start();
        timer.go();
        
        setMenuBar(new AppMenuBar(this));
        setSize(950, 950);
        addWindowListener(new WindowAdapter(){//dogadjaji nad prozorom
        	@Override 
        	public void windowClosing(WindowEvent e){ 
        		dispose(); 
        		}//dodati da li ste sigurni
        	});
        setVisible(true); 
        
        airportForm.getBtnAddAirport().addActionListener(this);
        flightForm.getBtnAddFlight().addActionListener(this);
        
        addActivityHooks(this);

    }
    
    private Component buildTop() {
        Panel north = new Panel(new BorderLayout());

        Panel leftBlock = new Panel(new GridLayout(2, 1));
        leftBlock.add(airportForm);
        leftBlock.add(flightForm);

        Panel rightTimer = new Panel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        rightTimer.add(new Label("Neaktivnost: "));
        rightTimer.add(labelTimer);

        north.add(leftBlock, BorderLayout.CENTER);
        north.add(rightTimer, BorderLayout.EAST);
        return north;
    }    
    private Component buildMap() {
        Panel south = new Panel(new BorderLayout());
        Runnable onSelectStart = new Runnable() {
            @Override
            public void run() {
                timer.pause();
            }
        };

        Runnable onSelectEnd = new Runnable() {
            @Override
            public void run() {
                timer.go();
            }
        };

        mapCanvas = new MapCanvas(onSelectStart, onSelectEnd);
        mapCanvas.setAirports(airports.getList());

        south.add(mapCanvas, BorderLayout.CENTER);
        south.add(airportsFilter, BorderLayout.EAST);  // << ovde

        south.setPreferredSize(new Dimension(100, 400));
        return south;
    }

    
    @Override public void actionPerformed(ActionEvent e) {
        timer.activity();
        try {
            Object src = e.getSource();
            if (src == airportForm.getBtnAddAirport()) addAirport();
            if (src == flightForm.getBtnAddFlight())   addFlight();
        } catch (Exception ex) {
            Dialogs.showError(this, ex.getMessage());
        }
    }
    
    private void addAirport() throws Exception {
    	String name = airportForm.getTfName().getText();
        String code = airportForm.getTfCode().getText();
        int x = validation.Validators.parseIntField(airportForm.getTfX().getText(), "Koordinata X");
        int y = validation.Validators.parseIntField(airportForm.getTfY().getText(), "Koordinata Y");
        Airport a = airports.addAirport(name, code, x, y); //dodavanje u niz aerodroma + validacija
        flightForm.getChFrom().add(a.getCode());
        flightForm.getChTo().add(a.getCode());
        airportForm.clearAirportInputs();
        refreshViews();
        airportsFilter.rebuild(airports, mapCanvas);
        mapCanvas.setAirports(airports.getList());
    }
    

    private void addFlight() throws Exception {
    	Choice chFrom = flightForm.getChFrom();
        Choice chTo   = flightForm.getChTo();
    	if (chFrom.getItemCount() < 2 || chTo.getItemCount() < 2) {
            throw new ValidationException("Moraju postojati najmanje dva aerodroma da bi uneli let.");
        }
        String fromCode = chFrom.getSelectedItem();
        String toCode   = chTo.getSelectedItem();
        Airport from = flights.getAirport(fromCode);
        Airport to   = flights.getAirport(toCode);
        LocalTime dep = ValidatorForFlights.validTime(flightForm.getTfDep().getText());   // parsira i validira
        int dur = validation.Validators.parseIntField(flightForm.getTfDur().getText(), "Trajanje (min)");
        validation.ValidatorForFlights.validDuration(dur);
        flights.addFlight(from, to, dep, dur); // dodavanje u niz letova + validacija
        flightForm.clearFlightInputs();
        refreshViews(); 
    }
    private void refreshViews() {
    	listsPanel.getTaAirports().setText("Naziv (Kod) [X,Y]\n"); // zaglavlje
        for (Airport a : airports) {
        	listsPanel.getTaAirports().append(a.toString() + "\n"); // koristi toString()
        }
        listsPanel.getTaFlights().setText("Od→Do Vreme (Trajanje)\n"); // zaglavlje

        for (Flight f : flights) {
        	listsPanel.getTaFlights().append(f.toString() + "\n");
        }
    }

    
    public void onSaveAirports() {
    	File f = Dialogs.chooseFile(this, FileDialog.SAVE);
        if (f == null) return;
        try {
            airportCsv.save(f, airports);
        } catch (Exception ex) {
            Dialogs.showError(this, "Greška pri čuvanju aerodroma: " + ex.getMessage());
        }
    }

    public void onLoadAirports() {
        File f = Dialogs.chooseFile(this, FileDialog.LOAD);
        if (f == null) return;
        try {
            airports.clear();   
            flightForm.getChFrom().removeAll(); 
            flightForm.getChTo().removeAll();

            airportCsv.load(f, airports);

            for (Airport a : airports) {
            	flightForm.getChFrom().add(a.getCode());
            	flightForm.getChTo().add(a.getCode());
            }
            refreshViews();
            airportsFilter.rebuild(airports, mapCanvas);
            mapCanvas.setAirports(airports.getList());
            mapCanvas.repaint();
        } catch (Exception ex) {
            Dialogs.showError(this, "Greška pri učitavanju aerodroma: " + ex.getMessage());
        }
    }

    public void onSaveFlights() {
        File f = Dialogs.chooseFile(this, FileDialog.SAVE);
        if (f == null) return;
        try {
            flightCsv.save(f, flights);
        } catch (Exception ex) {
            Dialogs.showError(this, "Greška pri čuvanju letova: " + ex.getMessage());
        }
    }

    public void onLoadFlights() {
        File f = Dialogs.chooseFile(this, FileDialog.LOAD);
        if (f == null) return;
        try {
            flights.clear();           
            flightCsv.load(f, flights);
            refreshViews();
        } catch (Exception ex) {
            Dialogs.showError(this, "Greška pri učitavanju letova: " + ex.getMessage());
        }
    }
    public Timer getTimer() { 
    	return this.timer; 
    }
    private void addActivityHooks(Container root) {
        for (Component c : root.getComponents()) {
            c.addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) { timer.activity(); }//pritisnuto dugme
            });
            c.addMouseMotionListener(new MouseMotionAdapter() {//kretanja misa
                @Override public void mouseMoved(MouseEvent e) { timer.activity(); }//kretanje bez pritiska misa
            });
            c.addMouseWheelListener(e -> timer.activity());//pomeren tockic na misu, klasa koja nema adaptera

            c.addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(KeyEvent e) { timer.activity(); }//tastatura
            });

            if (c instanceof Container) {
                addActivityHooks((Container) c);
            }

        }
    }
}
