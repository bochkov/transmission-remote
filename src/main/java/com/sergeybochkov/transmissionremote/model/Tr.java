package com.sergeybochkov.transmissionremote.model;

import com.sergeybochkov.transmissionremote.ui.elems.*;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class Tr {

    public static final int STATUS_PAUSED = 0;
    public static final int STATUS_CHECKED = 2;
    public static final int STATUS_QUEUED = 3;
    public static final int STATUS_DOWNLOAD = 4;
    public static final int STATUS_UPLOAD = 6;

    private Integer id;
    private Integer error;
    private String errorString;
    private Long eta;
    private String name;
    private Integer peersConnected;
    private Integer peersGettingFromUs;
    private Integer peersSendingToUs;
    private Double percentDone;
    private Double rateDownload;
    private Double rateUpload;
    private Long sizeWhenDone;
    private Integer status;
    private Double uploadRatio;

    public Integer id() {
        return id;
    }

    public Boolean completed() {
        return percentDone >= 1.0;
    }

    public HBox graphic(Control parent) {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(
                new NameElem(name).graphic(),
                new PeersSpeedElem(error, errorString, status, peersSendingToUs,
                        peersGettingFromUs, peersConnected, rateDownload, rateUpload).graphic(),
                new ProgressElem(error, status, percentDone).graphic(),
                new FileElem(status, sizeWhenDone, percentDone, uploadRatio, eta).graphic());

        HBox graphicBox = new HBox(10);
        //graphicBox.setMaxWidth(parent.getWidth() - 20);
        graphicBox.getChildren().addAll(
                new ImageElem(name).graphic(),
                vbox);
        return graphicBox;
    }
}
