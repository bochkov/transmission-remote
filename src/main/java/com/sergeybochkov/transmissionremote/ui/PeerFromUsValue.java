package com.sergeybochkov.transmissionremote.ui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public final class PeerFromUsValue<S, T>
        implements Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {
    @Override
    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        return null;
    }
}
