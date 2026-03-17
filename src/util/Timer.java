package util;

import java.awt.*;
import java.awt.event.*;


public class Timer extends Thread{
	private final Label label;
	private final Frame owner;
	
	private int s = 0;
	private boolean work = false;
    private TimeoutDialog dialog = null;
    private boolean dialogVisible = false;

    public Timer(Label label, Frame owner) {
        this.label = label;
        this.owner = owner;
        setDaemon(true);
    }
    
    @Override
    public void run() {
    	try {
    		while (!isInterrupted()) {//dok traje vreme i nije spolja prekinuta
    			synchronized(this) {
    				while(!work) {
    					wait();
    				}
    			}
    			label.setText(toString());
                if (s == 55) showWarningDialog();    
                else if (s >= 60) {
                	owner.dispose();
                    break;
                }

                sleep(1000);
                tick();
    		}
    		
    	}catch (InterruptedException ie) {
    	}
    }
    private void tick() {
        s++;
    }
    public synchronized void pause() { work = false; }

    
    public synchronized void go() {
    	work = true;
    	notify();
    }
    
    public synchronized void reset() {
    	s = 0;
    	if (dialog != null && dialog.isShowing()) {
    		dialog.setVisible(false);
    	}
    }
    public void activity() {
        if (dialogVisible) return; // ne resetuj timer ako je dijalog otvoren
    	reset();
    	go();
    }
    @Override
    public String toString() {
    	return String.format("%02d", s);
    }
    
    private void showWarningDialog() {
    	if (dialog == null) dialog = new TimeoutDialog(owner);
    	dialogVisible = true;
    	dialog.setVisible(true);   
    }
    
    private final class TimeoutDialog extends Dialog {
        Label lbl = new Label("Aplikacija će se zatvoriti za 5 sekundi. Želite li da nastavite?", Label.CENTER);
        private final Button nastavi = new Button("Nastavi");
        private final Button izadji  = new Button("Izadji");

        TimeoutDialog(Frame owner) {
            super(owner, "Neaktivnost", false);
            setLayout(new BorderLayout(10,10));
            add(lbl, BorderLayout.CENTER);
            Panel p = new Panel(new FlowLayout());
            p.add(nastavi); p.add(izadji);
            add(p, BorderLayout.SOUTH);

            nastavi.addActionListener(e -> {
                Timer.this.reset();
            	dialogVisible = false;
                setVisible(false);
            });
            izadji.addActionListener(e -> {
                setVisible(false);
                owner.dispose();
            });
            addWindowListener(new WindowAdapter() {
                @Override public void windowClosing(WindowEvent e) {
                    setVisible(false);
                }
            });

            setSize(360, 150);
            setLocationRelativeTo(owner);
        }
    }

}
