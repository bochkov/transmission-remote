package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.model.Peer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public final class BasePeerValue
        implements Callback<TableColumn.CellDataFeatures<Peer, Peer>, ObservableValue<Peer>> {

    @Override
    public ObservableValue<Peer> call(TableColumn.CellDataFeatures<Peer, Peer> param) {
        return new SimpleObjectProperty<>(param.getValue());
    }
}
