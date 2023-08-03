package com.sb.transmissionremote.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

import com.sb.transmissionremote.TransmissionRemote;
import com.sb.transmissionremote.ui.FrmAdd;
import cordelia.client.TrClient;

public final class AcAddTorrent extends AbstractAction {

    private final Frame owner;
    private final AtomicReference<TrClient> client;

    public AcAddTorrent(Frame owner, AtomicReference<TrClient> client) {
        super("Add torrent", TransmissionRemote.ICON_FOLDER_OPEN_O);
        this.owner = owner;
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new FrmAdd(owner, client).setVisible(true);
    }
}
