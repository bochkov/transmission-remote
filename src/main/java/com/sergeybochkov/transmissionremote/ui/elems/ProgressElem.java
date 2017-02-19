package com.sergeybochkov.transmissionremote.ui.elems;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

import static com.sergeybochkov.transmissionremote.model.Tr.*;

public final class ProgressElem implements Element {

    private final int error;
    private final int status;
    private final double percentDone;

    public ProgressElem(int error, int status, double percentDone) {
        this.error = error;
        this.status = status;
        this.percentDone = percentDone;
    }

    @Override
    public Node graphic() {
        ProgressBar pb = new ProgressBar(percentDone);
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

        HBox box = new HBox(10);
        //box.setMaxWidth(parent.getWidth() - 20);
        //pb.prefWidthProperty().bind(graphicBox.widthProperty().subtract(150));
        box.getChildren().addAll(pb,
                new Label(String.format("%.2f %%", percentDone * 100)));

        return box;
    }
}
