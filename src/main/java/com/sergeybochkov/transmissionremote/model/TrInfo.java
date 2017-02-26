package com.sergeybochkov.transmissionremote.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public final class TrInfo {

    private String name;
    private Double length;
    private Double complete;
    private String wanted;

    public ObservableValue<String> name() {
        return new SimpleStringProperty(name);
    }

    public ObservableValue<String> length() {
        return new SimpleStringProperty(
                new Size(length)
                        .toString());
    }

    public ObservableValue<String> complete() {
        return new SimpleStringProperty(
                new Size(complete).toString());
    }

    public ObservableValue<String> wanted() {
        return new SimpleStringProperty(wanted);
    }
}
