package com.sergeybochkov.transmissionremote.ui.elems;

import javafx.scene.Node;
import javafx.scene.control.Label;

public final class NameElem implements Element {

    public final String name;

    public NameElem(String name) {
        this.name = name;
    }

    @Override
    public Node graphic() {
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("header-font");
        return nameLabel;
    }
}
