package com.sergeybochkov.transmissionremote.ui;

import com.sergeybochkov.transmissionremote.model.TrInfo;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public final class FileWantedValue
        implements Callback<TreeTableColumn.CellDataFeatures<TrInfo, String>, ObservableValue<String>> {

    @Override
    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TrInfo, String> param) {
        return param.getValue().getValue().wanted();
    }
}
