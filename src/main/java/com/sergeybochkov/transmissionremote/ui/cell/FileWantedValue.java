package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.model.TorWrap;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public final class FileWantedValue
        implements Callback<TreeTableColumn.CellDataFeatures<TorWrap, String>, ObservableValue<Boolean>> {

    @Override
    public ObservableValue<Boolean> call(TreeTableColumn.CellDataFeatures<TorWrap, String> param) {
        return param.getValue().getValue().wanted();
    }
}
