package com.sergeybochkov.transmissionremote.model;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.ui.IconLabel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public final class TorWrap {

    private final int id;
    private final TorFile file;
    private final TorFStat stat;

    public TorWrap(int id, TorFile file, TorFStat stat) {
        this.id = id;
        this.file = file;
        this.stat = stat;
    }

    public int id() {
        return id;
    }

    public String name() {
        return file.name();
    }

    public ObservableValue<String> complete() {
        return new SimpleStringProperty(
                new Size(file.completed()).toString());
    }

    public ObservableValue<String> nameValue() {
        return new SimpleStringProperty(file.name());
    }

    public ObservableValue<String> length() {
        return new SimpleStringProperty(
                new Size(file.length())
                        .toString());
    }

    public ObservableValue<Boolean> wanted() {
        return new SimpleBooleanProperty(stat.wanted());
    }

    public IconLabel priorityGraphic() {
        IconLabel il = new IconLabel(TransmissionRemote.ICON_CIRCLE);
        switch (stat.priority()) {
            case -1:
                il.setStyle("-fx-text-fill: yellow");
                break;
            case 0:
                il.setStyle("-fx-text-fill: green");
                break;
            case 1:
                il.setStyle("-fx-text-fill: red");
                break;
        }
        return il;
    }
}
