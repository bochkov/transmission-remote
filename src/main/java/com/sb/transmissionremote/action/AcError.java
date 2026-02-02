package com.sb.transmissionremote.action;

import com.sb.transmissionremote.TransmissionRemote;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
public abstract class AcError extends AbstractAction {

    protected AcError(String text) {
        super(text);
    }

    protected AcError(String text, Icon icon) {
        super(text, icon);
    }

    protected void alert(Exception ex) {
        LOG.warn(ex.getMessage(), ex);
        JOptionPane.showMessageDialog(null, ex.getMessage(), TransmissionRemote.APP_NAME, JOptionPane.ERROR_MESSAGE);
    }
}
