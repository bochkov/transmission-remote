package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.helpers.Helpers;
import com.sergeybochkov.transmissionremote.helpers.Settings;
import com.sergeybochkov.transmissionremote.helpers.TorrentFieldWrap;
import javafx.scene.control.*;
import javafx.util.Callback;


public class PriorityFileViewFactory<S, T> implements Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> {

    @Override
    @SuppressWarnings("unchecked")
    public TreeTableCell<S, T> call(TreeTableColumn<S, T> param) {
        return new TreeTableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                if (item == getItem())
                    return;
                super.updateItem(item, empty);
                if (item == null) {
                    super.setText(null);
                    super.setGraphic(null);
                }
                else if (item instanceof TorrentFieldWrap) {
                    TorrentFieldWrap file = (TorrentFieldWrap) item;
                    if (file.getInnerId() != null){
                        Label view = Helpers.createIcon(Settings.ICON_CIRCLE);
                        if (file.getPriority() != null) {
                            switch (file.getPriority()) {
                                case -1:
                                    view.setStyle("-fx-text-fill: yellow");
                                    break;
                                case 0:
                                    view.setStyle("-fx-text-fill: green");
                                    break;
                                case 1:
                                    view.setStyle("-fx-text-fill: red");
                                    break;
                            }
                        }
                        super.setGraphic(view);
                    }
                    super.setText(null);
                }
                else {
                    super.setGraphic(null);
                    super.setText(item.toString());
                }
            }
        };
    }
}
