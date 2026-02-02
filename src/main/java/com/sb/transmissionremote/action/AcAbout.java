package com.sb.transmissionremote.action;

import com.sb.transmissionremote.TransmissionRemote;
import com.sb.transmissionremote.ui.FrmAbout;

import javax.swing.*;
import java.awt.*;
import java.awt.desktop.AboutEvent;
import java.awt.desktop.AboutHandler;
import java.awt.event.ActionEvent;

public final class AcAbout extends AbstractAction implements AboutHandler {

    private final Frame owner;

    private static JDialog dialogInstance;

    public AcAbout(Frame owner) {
        super("About", TransmissionRemote.ICON_FLASK);
        this.owner = owner;
    }

    private synchronized JDialog dialogInst() {
        if (dialogInstance == null)
            dialogInstance = new FrmAbout(owner);
        return dialogInstance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dialogInst().setVisible(true);
    }

    @Override
    public void handleAbout(AboutEvent e) {
        dialogInst().setVisible(true);
    }
}
