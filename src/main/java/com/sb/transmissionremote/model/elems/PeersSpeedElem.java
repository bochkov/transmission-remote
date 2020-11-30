package com.sb.transmissionremote.model.elems;

import java.awt.*;
import javax.swing.*;

import com.sb.transmissionremote.model.Tor;

public final class PeersSpeedElem implements Element {

    private final int error;
    private final String errorString;
    private final int status;
    private final PeersLabel peers;

    public PeersSpeedElem(int error, String errorString, int status, PeersLabel peers) {
        this.error = error;
        this.errorString = errorString;
        this.status = status;
        this.peers = peers;
    }

    @Override
    public JComponent graphic() {
        JLabel peersAndSpeed = new JLabel();
        if (error != 0) {
            peersAndSpeed.setForeground(Color.RED);
            peersAndSpeed.setText(errorString);
        } else {
            peersAndSpeed.setForeground(Color.GRAY);
            switch (status) {
                case Tor.STATUS_DOWNLOAD:
                    peersAndSpeed.setText(peers.downDesc());
                    break;
                case Tor.STATUS_UPLOAD:
                    peersAndSpeed.setText(peers.upDesc());
                    break;
                case Tor.STATUS_PAUSED:
                    peersAndSpeed.setText("Paused");
                    break;
                case Tor.STATUS_CHECKED:
                    peersAndSpeed.setText("Verifying local data");
                    break;
                case Tor.STATUS_QUEUED:
                    peersAndSpeed.setText("Queued");
                    break;
                default:
                    break;
            }
        }
        peersAndSpeed.setFont(new Font("SansSerif", Font.PLAIN, 11));
        return peersAndSpeed;
    }
}
