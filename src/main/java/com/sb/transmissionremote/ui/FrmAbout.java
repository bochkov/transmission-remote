package com.sb.transmissionremote.ui;

import com.sb.transmissionremote.TransmissionRemote;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Calendar;

public final class FrmAbout extends JDialog {

    private static final String DESC = "<html><center><h3>%s</h3>Remote client for Transmission daemon<br><br>%s";
    private static final String MAIL = "<html><a href=''>bochkov.sa@gmail.com</a>";
    private static final String WEB = "<html><a href=''>https://sergeybochkov.com</a>";

    public FrmAbout(Frame owner) {
        super(owner, "О программе", true);
        setResizable(false);
        setLayout(new MigLayout("fillX, wrap 1, insets 30 10 10 10", "center"));

        add(new JLabel(TransmissionRemote.LOGO), "span");
        add(new JLabel(String.format(DESC, TransmissionRemote.APP_NAME, years())));

        var mailLabel = new JLabel(MAIL, TransmissionRemote.ICON_ENVELOPE_O, SwingConstants.CENTER);
        add(mailLabel);
        mailLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mailLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().mail(URI.create("mailto:bochkov.sa@gmail.com?subject=Transmission%20Remote"));
                } catch (Exception ex) {
                    //
                }
            }
        });

        var webLabel = new JLabel(WEB, TransmissionRemote.ICON_LINK, SwingConstants.CENTER);
        webLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        webLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(URI.create("https://sergeybochkov.com"));
                } catch (Exception ex) {
                    //
                }
            }
        });
        add(webLabel);

        setPreferredSize(new Dimension(350, 400));
        pack();
        setLocationRelativeTo(owner);
    }

    private String years() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String curYear = year > 2013 ? String.format("-%s", year) : "";
        return String.format("Sergey Bochkov, 2013%s", curYear);
    }
}
