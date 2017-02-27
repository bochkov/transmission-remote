package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.model.Peer;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public final class PeerToUsValue
        implements Callback<TableColumn.CellDataFeatures<Peer, String>, ObservableValue<String>> {
    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<Peer, String> param) {
        return param.getValue().toUs();
    }
}
