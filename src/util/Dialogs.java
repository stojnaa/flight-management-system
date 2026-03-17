package util;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public final class Dialogs {
    private Dialogs() {}

    public static void showError(Frame owner, String msg) {
        Dialog d = new Dialog(owner, "Greška", true);
        d.setLayout(new BorderLayout());
        d.add(new Label(msg), BorderLayout.CENTER);
        Button ok = new Button("U redu");
        ok.addActionListener(e -> d.setVisible(false));
        d.add(ok, BorderLayout.SOUTH);
        d.setSize(420, 140);
        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { d.setVisible(false); }
        });
        d.setLocationRelativeTo(owner);
        d.setVisible(true);
    }
    public static File chooseFile(Frame owner, int mode) {
        FileDialog fd = new FileDialog(owner,
                (mode == FileDialog.SAVE ? "Sačuvaj" : "Učitaj"), mode);
        fd.setVisible(true);
        String dir = fd.getDirectory();
        String file = fd.getFile();
        if (dir == null || file == null) return null;
        return new File(dir, file);
    }
}
