package com.sb.transmissionremote.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.sb.transmissionremote.TransmissionRemote;
import com.sb.transmissionremote.ui.FrmSession;
import com.sb.transmissionremote.util.Callback;

public final class AcChangeSession extends AbstractAction {

    private final Frame owner;
    private final Callback callback;

    public AcChangeSession(Frame owner, Callback callback) {
        super("Change session", TransmissionRemote.ICON_GLOBE);
        this.owner = owner;
        this.callback = callback;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new FrmSession(owner, callback).setVisible(true);
    }

}
