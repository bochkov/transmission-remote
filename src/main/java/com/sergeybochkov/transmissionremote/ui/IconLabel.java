package com.sergeybochkov.transmissionremote.ui;

import javafx.scene.control.Label;

public final class IconLabel extends Label {

    public IconLabel(String label) {
        super(label);
        setStyle("-fx-font-size: 14px;");
        getStyleClass().add("icons");
    }
}
