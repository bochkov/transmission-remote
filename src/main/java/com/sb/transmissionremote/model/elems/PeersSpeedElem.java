package com.sb.transmissionremote.model.elems;

import cordelia.jsonrpc.res.RsTorrentGet;

import javax.swing.*;
import java.awt.*;

public final class PeersSpeedElem implements Element {

    private final Long error;
    private final String errorString;
    private final RsTorrentGet.Status status;
    private final PeersLabel peers;

    public PeersSpeedElem(RsTorrentGet.Torrents t) {
        this.error = t.getError();
        this.errorString = t.getErrorString();
        this.status = t.getStatus();
        this.peers = new PeersLabel(
                t.getPeersSendingToUs(),
                t.getPeersGettingFromUs(),
                t.getPeersConnected(),
                t.getRateDownload(),
                t.getRateUpload()
        );
    }

    @Override
    public JComponent graphic() {
        var peersAndSpeed = new JLabel();
        if (error != 0) {
            peersAndSpeed.setForeground(Color.RED);
            peersAndSpeed.setText(errorString);
        } else {
            peersAndSpeed.setForeground(Color.GRAY);
            switch (status) {
                case DOWNLOADING -> peersAndSpeed.setText(peers.downDesc());
                case SEEDING -> peersAndSpeed.setText(peers.upDesc());
                case STOPPED -> peersAndSpeed.setText("Paused");
                case VERIFYING -> peersAndSpeed.setText("Verifying local data");
                case QUEUED_TO_DOWNLOAD, QUEUED_TO_SEED, QUEUED_TO_VERIFY -> peersAndSpeed.setText("Queued");
                default -> {
                    // do nothing
                }
            }
        }
        peersAndSpeed.setFont(peersAndSpeed.getFont().deriveFont(11.0f));
        return peersAndSpeed;
    }
}
