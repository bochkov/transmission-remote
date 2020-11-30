package com.sb.transmissionremote.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.*;

import com.sb.transmissionremote.TransmissionRemote;

public final class AcExit extends AbstractAction {

    private final Frame owner;

    public AcExit(Frame owner) {
        super("Exit", TransmissionRemote.ICON_SIGN_OUT);
        this.owner = owner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        owner.dispatchEvent(new WindowEvent(owner, WindowEvent.WINDOW_CLOSING));
    }

}
