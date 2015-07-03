package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.helpers.Helpers;
import com.sergeybochkov.transmissionremote.helpers.Settings;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentField;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;


public class PeerViewCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    @Override
    @SuppressWarnings("unchecked")
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                if (item == getItem())
                    return;
                super.updateItem(item, empty);
                if (item == null) {
                    super.setText(null);
                    super.setGraphic(null);
                }
                else if (item instanceof TorrentField.Peer) {
                    TorrentField.Peer peer = (TorrentField.Peer) item;
                    Label view = null;
                    if (peer.getEncrypted()) {
                        view = Helpers.createIcon(Settings.ICON_LOCK);
                        view.setStyle("-fx-text-fill: gray");
                    }
                    super.setText(null);
                    super.setGraphic(view);
                }
                else {
                    super.setText(item.toString());
                    super.setGraphic(null);
                }
            }
        };
    }
}
