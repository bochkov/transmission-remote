package com.sergeybochkov.transmissionremote.ui.elems;

import com.sergeybochkov.transmissionremote.model.HumanSize;
import com.sergeybochkov.transmissionremote.model.HumanTime;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import static com.sergeybochkov.transmissionremote.model.Tor.*;

public final class FileElem implements Element {

    private final int status;
    private final long sizeWhenDone;
    private final double percentDone;
    private final long eta;
    private final double uploadRatio;

    public FileElem(int status, long sizeWhenDone, double percentDone, double uploadRatio, long eta) {
        this.status = status;
        this.sizeWhenDone = sizeWhenDone;
        this.percentDone = percentDone;
        this.uploadRatio = uploadRatio;
        this.eta = eta;
    }

    @Override
    public Node graphic() {
        Label fileLabel = new Label();
        fileLabel.setId("files");
        fileLabel.getStyleClass().add("custom-font");
        fileLabel.setTextFill(Color.GRAY);
        HumanSize total = new HumanSize(sizeWhenDone * percentDone);
        switch (status) {
            case STATUS_DOWNLOAD:
                fileLabel.setText(String.format("%s of %s — %s remaining",
                        total, new HumanSize(sizeWhenDone), new HumanTime(eta)));
                break;
            case STATUS_UPLOAD:
                fileLabel.setText(String.format("%s, uploaded %s (Ratio %.2f)",
                        total, new HumanSize(sizeWhenDone * uploadRatio), uploadRatio));
                break;
            case STATUS_CHECKED:
                fileLabel.setText(String.format("%s of %s", total, new HumanSize(sizeWhenDone)));
                break;
            default:
                break;
        }
        return fileLabel;
    }
}
