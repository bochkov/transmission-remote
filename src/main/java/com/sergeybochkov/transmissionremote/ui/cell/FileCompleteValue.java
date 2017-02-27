package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.model.TorWrap;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public final class FileCompleteValue
        implements Callback<TreeTableColumn.CellDataFeatures<TorWrap, String>, ObservableValue<String>> {

    @Override
    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TorWrap, String> param) {
        return param.getValue().getValue().complete();
    }
}
