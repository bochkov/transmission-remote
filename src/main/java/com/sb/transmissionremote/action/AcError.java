package com.sb.transmissionremote.action;

import javax.swing.*;

import com.sb.transmissionremote.TransmissionRemote;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AcError extends AbstractAction {

    public AcError(String text) {
        super(text);
    }

    public AcError(String text, Icon icon) {
        super(text, icon);
    }

    protected void alert(Exception ex) {
        LOG.warn(ex.getMessage(), ex);
        JOptionPane.showMessageDialog(null, ex.getMessage(), TransmissionRemote.APP_NAME, JOptionPane.ERROR_MESSAGE);
    }
}
