package com.sergeybochkov.transmissionremote.ui;

import com.sergeybochkov.transmissionremote.model.Torrent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TorrentCell<T> implements Callback<ListView<T>, ListCell<T>> {
    @Override
    public ListCell<T> call(final ListView<T> listView) {
        return new ListCell<T>(){
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (item instanceof Torrent) {
                    setText(null);
                    setGraphic(((Torrent) item).graphic(listView));
                } else {
                    setText(item == null ? "null" : item.toString());
                    setGraphic(null);
                }
            }
        };
    }
}