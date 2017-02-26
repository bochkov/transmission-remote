package com.sergeybochkov.transmissionremote.ui;

import com.sergeybochkov.transmissionremote.model.Peer;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public final class PeerEncryptionCell<S, T>
        implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                if (item != getItem()) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        super.setText(null);
                        super.setGraphic(null);
                    } else if (item instanceof Peer) {
                        super.setText(null);
                        super.setGraphic(((Peer) item).encryptedGraphic());
                    } else {
                        super.setText(item.toString());
                        super.setGraphic(null);
                    }
                }
            }
        };
    }
}
