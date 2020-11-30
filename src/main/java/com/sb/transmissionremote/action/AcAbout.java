package com.sb.transmissionremote.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.sb.transmissionremote.TransmissionRemote;
import com.sb.transmissionremote.ui.FrmAbout;

public final class AcAbout extends AbstractAction {

    private final Frame owner;

    public AcAbout(Frame owner) {
        super("About", TransmissionRemote.ICON_FLASK);
        this.owner = owner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new FrmAbout(owner).setVisible(true);
    }
}
