package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.model.Tor;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public final class TorrentCell<T> implements Callback<ListView<T>, ListCell<T>> {
    @Override
    public ListCell<T> call(final ListView<T> listView) {
        return new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (item instanceof Tor) {
                    setText(null);
                    setGraphic(((Tor) item).graphic(listView));
                } else {
                    setText(item == null ? "null" : item.toString());
                    setGraphic(null);
                }
            }
        };
    }
}