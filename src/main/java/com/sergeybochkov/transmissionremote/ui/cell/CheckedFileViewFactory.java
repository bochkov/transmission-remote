package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.helpers.TorrentFieldWrap;
import javafx.scene.control.*;
import javafx.util.Callback;


public class CheckedFileViewFactory<S, T> implements Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> {

    @Override
    public TreeTableCell<S, T> call(TreeTableColumn<S, T> param) {
        return new TreeTableCell<S, T>() {

            private CheckBox checkBox = new CheckBox();

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
                    Boolean selected = file.getWanted();
                    checkBox.textProperty().setValue(file.getName());
                    if (file.getInnerId() != null)
                        // если не родительская папка, т.е. ветка
                        checkBox.selectedProperty().setValue(selected);
                    super.setGraphic(checkBox);
                    super.setText(null);
                }
                else {
                    super.setGraphic(null);
                    super.setText(null);
                }
            }
        };
    }
}