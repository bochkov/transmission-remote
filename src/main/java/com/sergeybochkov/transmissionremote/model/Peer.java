package com.sergeybochkov.transmissionremote.model;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.ui.IconLabel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

public final class Peer {

    private String address;
    private Boolean clientIsChoked;
    private Boolean clientIsInterested;
    private String clientName;
    private String flagStr;
    private Boolean isDownloadingFrom;
    private Boolean isEncrypted;
    private Boolean isIncoming;
    private Boolean isUTP;
    private Boolean isUploadingTo;
    private Boolean peerIsChoked;
    private Boolean peerIsInterested;
    private Integer port;
    private Double progress;
    private Double rateToClient;
    private Double rateToPeer;

    public ObservableValue<String> address() {
        return new SimpleStringProperty(address);
    }

    public ObservableValue<String> client() {
        return new SimpleStringProperty(clientName);
    }

    public ObservableValue<Boolean> encrypted() {
        return new SimpleBooleanProperty(isEncrypted);
    }

    public Label encryptedGraphic() {
        return new IconLabel(isEncrypted ? TransmissionRemote.ICON_LOCK : "");
    }

    public ObservableValue<String> fromUs() {
        return new SimpleStringProperty(new Speed(rateToPeer).toString());
    }

    public ObservableValue<String> toUs() {
        return new SimpleStringProperty(new Speed(rateToClient).toString());
    }
}
