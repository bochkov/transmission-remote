package com.sergeybochkov.transmissionremote.ui.elems;

import com.sergeybochkov.transmissionremote.model.Speed;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import static com.sergeybochkov.transmissionremote.model.Tr.*;

public final class PeersSpeedElem implements Element {

    private final int error;
    private final String errorString;
    private final int status;
    private final int peersSendingToUs;
    private final int peersGettingFromUs;
    private final int peersConnected;
    private final double rateDownload;
    private final double rateUpload;

    public PeersSpeedElem(int error,
                          String errorString,
                          int status,
                          int peersSendingToUs,
                          int peersGettingFromUs,
                          int peersConnected,
                          double rateDownload,
                          double rateUpload) {
        this.error = error;
        this.errorString = errorString;
        this.status = status;
        this.peersSendingToUs = peersSendingToUs;
        this.peersGettingFromUs = peersGettingFromUs;
        this.peersConnected = peersConnected;
        this.rateDownload = rateDownload;
        this.rateUpload = rateUpload;
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
                    peersAndSpeed.setText(String.format("Downloading from %d of %d peers — ↓ %s ↑ %s",
                            peersSendingToUs, peersConnected,
                            new Speed(rateDownload), new Speed(rateUpload)));
                    break;
                case STATUS_UPLOAD:
                    peersAndSpeed.setText(String.format("Seeding to %d of %d peers — ↑ %s",
                            peersGettingFromUs, peersConnected, new Speed(rateUpload)));
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
            }
        }
        return peersAndSpeed;
    }
}
