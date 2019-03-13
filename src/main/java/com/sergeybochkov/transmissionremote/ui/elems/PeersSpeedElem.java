package com.sergeybochkov.transmissionremote.ui.elems;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import static com.sergeybochkov.transmissionremote.model.Tor.*;

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
    public Node graphic() {
        Label peersAndSpeed = new Label();
        peersAndSpeed.getStyleClass().add("custom-font");
        peersAndSpeed.setId("peers");
        if (error != 0) {
            peersAndSpeed.setTextFill(Color.RED);
            peersAndSpeed.setText(errorString);
        } else {
            peersAndSpeed.setTextFill(Color.GRAY);
            switch (status) {
                case STATUS_DOWNLOAD:
                    peersAndSpeed.setText(peers.downDesc());
                    break;
                case STATUS_UPLOAD:
                    peersAndSpeed.setText(peers.upDesc());
                    break;
                case STATUS_PAUSED:
                    peersAndSpeed.setText("Paused");
                    break;
                case STATUS_CHECKED:
                    peersAndSpeed.setText("Verifying local data");
                    break;
                case STATUS_QUEUED:
                    peersAndSpeed.setText("Queued");
                    break;
                default:
                    break;
            }
        }
        return peersAndSpeed;
    }
}
