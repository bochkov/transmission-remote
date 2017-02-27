package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.model.TorWrap;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public final class FileBaseValue
        implements Callback<TreeTableColumn.CellDataFeatures<TorWrap, TorWrap>, ObservableValue<TorWrap>> {

    @Override
    public ObservableValue<TorWrap> call(TreeTableColumn.CellDataFeatures<TorWrap, TorWrap> param) {
        return new SimpleObjectProperty<>(param.getValue().getValue());
    }
}
