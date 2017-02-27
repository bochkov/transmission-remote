package com.sergeybochkov.transmissionremote.ui.elems;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

import static com.sergeybochkov.transmissionremote.model.Tor.*;

public final class ProgressElem implements Element {

    private final int error;
    private final int status;
    private final double percentDone;
    private final Control parent;

    public ProgressElem(int error, int status, double percentDone, Control parent) {
        this.error = error;
        this.status = status;
        this.percentDone = percentDone;
        this.parent = parent;
    }

    @Override
    public Node graphic() {
        ProgressBar pb = new ProgressBar(percentDone);
        Label label = new Label(String.format("%.2f %%", percentDone * 100));
        pb.prefWidthProperty().bind(parent.widthProperty()
                .subtract(20)
                .subtract(64)
                .subtract(20)
                .subtract(20)
                // pb width
                .subtract(20)
                .subtract(20)
                .subtract(label.getPrefWidth())
                .subtract(20));
        if (error != 0) {
            pb.setStyle("-fx-accent: red;");
        } else {
            switch (status) {
                case STATUS_DOWNLOAD:
                    break;
                case STATUS_UPLOAD:
                    pb.setStyle("-fx-accent: #00A600;");
                    break;
                case STATUS_PAUSED:
                    pb.setStyle("-fx-accent: lightgray");
                    break;
                case STATUS_CHECKED:
                    pb.setStyle("-fx-accent: gray;");
                    break;
                case STATUS_QUEUED:
                    pb.setStyle("-fx-accent: darkgray;");
                    break;
                default:
                    break;
            }
        }
        return new HBox(10,
                pb,
                label);
    }
}
