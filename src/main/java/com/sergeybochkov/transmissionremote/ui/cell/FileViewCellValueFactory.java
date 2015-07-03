package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.helpers.Helpers;
import com.sergeybochkov.transmissionremote.helpers.TorrentFieldWrap;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public class FileViewCellValueFactory<S, T> implements Callback<TreeTableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ObservableValue<T> call(TreeTableColumn.CellDataFeatures<S, T> param) {

        TorrentFieldWrap t = (TorrentFieldWrap) param.getValue().getValue();
        if (t == null)
            return null;

        if (data == null)
            return (ObservableValue<T>) new SimpleObjectProperty(t);

        SimpleObjectProperty retProp = new SimpleObjectProperty<>();
        switch (data) {
            case "name":
                retProp.setValue(t.getName());
                break;
            case "length":
                Long length = t.getLength();
                if (length != null)
                    retProp.setValue(Helpers.humanizeSize(length));
                break;
            case "complete":
                Long bytesComplete = t.getBytesComplete();
                if (bytesComplete != null)
                    retProp.setValue(Helpers.humanizeSize(bytesComplete));
                break;
            case "wanted":
                retProp.setValue(t.getWanted());
                break;
        }
        return (ObservableValue<T>) retProp;
    }
}
