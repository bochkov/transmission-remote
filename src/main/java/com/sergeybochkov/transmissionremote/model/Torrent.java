package com.sergeybochkov.transmissionremote.model;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.InputStream;

public final class Torrent {

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
        HBox box = new HBox(10);
        box.setMaxWidth(parent.getWidth() - 20);

        Label nameLabel = new Label();
        nameLabel.getStyleClass().add("header-font");
        nameLabel.setText(name);

        Label peersAndSpeed = new Label();
        peersAndSpeed.getStyleClass().add("custom-font");
        peersAndSpeed.setId("peers");
        if (error != 0) {
            peersAndSpeed.setText(errorString);
            peersAndSpeed.setTextFill(Color.RED);
        } else {
            String statusStr = "";
            switch (status) {
                case STATUS_DOWNLOAD:
                    statusStr = String.format("Downloading from %d of %d peers — ↓ %s ↑ %s",
                            peersSendingToUs, peersConnected,
                            new Speed(rateDownload), new Speed(rateUpload));
                    break;

                case STATUS_UPLOAD:
                    statusStr = String.format("Seeding to %d of %d peers — ↑ %s",
                            peersGettingFromUs, peersConnected, new Speed(rateUpload));
                    break;

                case STATUS_PAUSED:
                    statusStr = "Paused";
                    break;

                case STATUS_CHECKED:
                    statusStr = "Verifying local data";
                    break;

                case STATUS_QUEUED:
                    statusStr = "Queued";
                    break;
            }
            peersAndSpeed.setText(statusStr);
            peersAndSpeed.setTextFill(Color.GRAY);
        }

        String str = "";
        Size total = new Size(sizeWhenDone * percentDone);
        switch (status) {
            case STATUS_DOWNLOAD:
                str = String.format("%s of %s — %s remaining",
                        total, new Size(sizeWhenDone), new Time(eta));
                break;

            case STATUS_UPLOAD:
                str = String.format("%s, uploaded %s (Ratio %.2f)",
                        total, new Size(sizeWhenDone * uploadRatio), uploadRatio);
                break;

            case STATUS_CHECKED:
                str = String.format("%s of %s", total, new Size(sizeWhenDone));
                break;
        }
        Label fileLabel = new Label(str);
        fileLabel.getStyleClass().add("custom-font");
        fileLabel.setTextFill(Color.GRAY);

        ProgressBar pb = new ProgressBar();
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
        Label progressLabel = new Label();
        box.getChildren().addAll(pb, progressLabel);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(nameLabel, peersAndSpeed, box, fileLabel);

        ImageView image = new ImageView();
        String extension = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        InputStream stream = getClass().getResourceAsStream("/filetypes/" + extension + ".png");
        if (stream == null)
            stream = getClass().getResourceAsStream("/filetypes/folder.png");
        if (stream != null)
            image.setImage(new Image(stream));

        HBox graphicBox = new HBox(10);
        graphicBox.getChildren().addAll(image, vbox);
        graphicBox.setMaxWidth(parent.getWidth() - 20);

        pb.prefWidthProperty().bind(graphicBox.widthProperty().subtract(150));
        pb.progressProperty().setValue(percentDone);

        DoubleBinding percent = pb.progressProperty().multiply(100);
        SimpleStringProperty ssp = new SimpleStringProperty(String.format("%.2f %%", percent.getValue()));
        progressLabel.textProperty().bind(ssp);

        return graphicBox;
    }
}
