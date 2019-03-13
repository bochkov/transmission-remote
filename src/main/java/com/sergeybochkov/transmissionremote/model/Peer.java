package com.sergeybochkov.transmissionremote.model;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.ui.IconLabel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

public final class Peer {

    private String address;
    private String clientName;
    private Boolean isEncrypted;
    private Double rateToClient;
    private Double rateToPeer;

    public ObservableValue<String> address() {
        return new SimpleStringProperty(address);
    }

    public ObservableValue<String> client() {
        return new SimpleStringProperty(clientName);
    }

    public Label encryptedGraphic() {
        return new IconLabel(isEncrypted ? TransmissionRemote.ICON_LOCK : "");
    }

    public ObservableValue<String> fromUs() {
        return new SimpleStringProperty(new HumanSpeed(rateToPeer).toString());
    }

    public ObservableValue<String> toUs() {
        return new SimpleStringProperty(new HumanSpeed(rateToClient).toString());
    }
}
