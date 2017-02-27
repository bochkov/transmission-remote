package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.model.TorWrap;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public final class CheckedFileCell<S, T>
        implements Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> {

    @Override
    public TreeTableCell<S, T> call(TreeTableColumn<S, T> param) {
        return new CheckedCell();
    }

    private class CheckedCell extends TreeTableCell<S, T> {

        private final CheckBox cb = new CheckBox();

        @Override
        protected void updateItem(T item, boolean empty) {
            if (item != getItem()) {
                super.updateItem(item, empty);
                if (item == null) {
                    super.setText(null);
                    super.setGraphic(null);
                } else if (item instanceof TorWrap) {
                    TorWrap wrap = (TorWrap) item;
                    cb.textProperty().setValue(wrap.name());
                    cb.selectedProperty().setValue(wrap.wanted().getValue());
                    super.setGraphic(cb);
                    super.setText(null);
                } else {
                    super.setGraphic(null);
                    super.setText(null);
                }
            }
        }
    }
}
