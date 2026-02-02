package com.sb.transmissionremote.action;

import com.sb.transmissionremote.TransmissionRemote;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class AcInfo extends AbstractAction {

    public AcInfo() {
        super("Info", TransmissionRemote.ICON_INFO_CIRCLE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // show info
    }
}
