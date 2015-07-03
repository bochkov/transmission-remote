package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.helpers.Helpers;
import com.sergeybochkov.transmissionremote.helpers.Settings;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentField;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class PeerViewCellValueFactory<S, T> implements Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        SimpleObjectProperty retProp = new SimpleObjectProperty();
        if (param.getValue() instanceof TorrentField.Peer) {
            TorrentField.Peer peer = (TorrentField.Peer) param.getValue();
            switch (data) {
                case "rateToPeer":
                    retProp.setValue(Helpers.humanizeSpeed(peer.getRateToPeer()));
                    break;
                case "rateToClient":
                    retProp.setValue(Helpers.humanizeSpeed(peer.getRateToClient()));
                    break;
                case "isEncrypted":
                    Label label = Helpers.createIcon(Settings.ICON_LOCK);
                    label.setStyle("-fx-text-fill: gray");
                    retProp.setValue(label);
                    break;
                default:
                    retProp.setValue(peer);
                    break;
            }
        }

        return (ObservableValue<T>) retProp;
    }
}
