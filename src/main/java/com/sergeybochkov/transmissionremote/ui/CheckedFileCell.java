package com.sergeybochkov.transmissionremote.ui;

import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public final class CheckedFileCell<S, T>
        implements Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> {

    @Override
    public TreeTableCell<S, T> call(TreeTableColumn<S, T> param) {
        return new TreeTableCell<S, T>() {

        };
    }
}
