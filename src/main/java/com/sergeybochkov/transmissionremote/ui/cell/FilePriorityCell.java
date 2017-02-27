package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.model.TorWrap;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public final class FilePriorityCell<S, T>
        implements Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> {

    @Override
    public TreeTableCell<S, T> call(TreeTableColumn<S, T> param) {
        return new PriorityCell();
    }

    public class PriorityCell extends TreeTableCell<S, T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            if (item != getItem()) {
                super.updateItem(item, empty);
                if (item == null) {
                    super.setText(null);
                    super.setGraphic(null);
                } else if (item instanceof TorWrap) {
                    super.setText(null);
                    super.setGraphic(((TorWrap) item).priorityGraphic());
                } else {
                    super.setGraphic(null);
                    super.setText(item.toString());
                }
            }
        }
    }
}
