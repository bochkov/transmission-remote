package com.sb.transmissionremote.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import com.sb.transmissionremote.TransmissionRemote;

public final class AcInfo extends AbstractAction {

    public AcInfo() {
        super("Info", TransmissionRemote.ICON_INFO_CIRCLE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // show info
    }
}
