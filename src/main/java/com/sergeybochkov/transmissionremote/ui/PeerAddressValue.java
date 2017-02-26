package com.sergeybochkov.transmissionremote.ui;

import com.sergeybochkov.transmissionremote.model.Peer;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public final class PeerAddressValue
        implements Callback<TableColumn.CellDataFeatures<Peer, String>, ObservableValue<String>> {

    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<Peer, String> param) {
        return param.getValue().address();
    }
}
