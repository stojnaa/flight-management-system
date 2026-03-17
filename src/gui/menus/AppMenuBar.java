package gui.menus;

import java.awt.*;
import java.awt.event.*;
import static java.awt.event.KeyEvent.*;
import gui.MainFrame;

public class AppMenuBar extends MenuBar {
	private final MainFrame owner;

    public AppMenuBar(MainFrame owner) {
        this.owner = owner;
		// --- SAVE meni ---
        Menu mSave = new Menu("Save");

        MenuItem miSaveAirports = new MenuItem("Save Airports");
        miSaveAirports.addActionListener(e -> { 
        	owner.getTimer().activity(); //
        	owner.onSaveAirports(); });

        MenuItem miSaveFlights = new MenuItem("Save Flights");
        miSaveFlights.addActionListener(e -> { 
        	owner.getTimer().activity();
        	owner.onSaveFlights(); });

        mSave.add(miSaveAirports);
        mSave.add(miSaveFlights);

        // --- LOAD meni ---
        Menu mLoad = new Menu("Load");

        MenuItem miLoadAirports = new MenuItem("Load Airports");
        miLoadAirports.addActionListener(e -> {
        	owner.getTimer().activity();
        	owner.onLoadAirports(); });

        MenuItem miLoadFlights = new MenuItem("Load Flights");
        miLoadFlights.addActionListener(e -> { 
        	owner.getTimer().activity();
        	owner.onLoadFlights(); });

        mLoad.add(miLoadAirports);
        mLoad.add(miLoadFlights);

        add(mSave);
        add(mLoad);
    }
}
