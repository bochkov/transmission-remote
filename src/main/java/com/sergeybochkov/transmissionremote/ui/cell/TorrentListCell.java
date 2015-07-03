package com.sergeybochkov.transmissionremote.ui.cell;

import com.sergeybochkov.transmissionremote.helpers.Helpers;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentField;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

import java.io.InputStream;

public class TorrentListCell<T> implements Callback<ListView<T>, ListCell<T>> {

    private TorrentCellData data = new TorrentCellData();

    @Override
    public ListCell<T> call(ListView<T> p) {
        return new ListCell<T>(){
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                }
                else if (item instanceof TorrentField) {
                    setText(null);
                    data.setFields((TorrentField) item);
                    setGraphic(data.getBox());
                }
                else {
                    setText(item == null ? "null" : item.toString());
                    setGraphic(null);
                }
            }
        };
    }

    private class TorrentCellData {

        public static final int STATUS_PAUSED = 0;
        public static final int STATUS_CHECKED = 2;
        public static final int STATUS_QUEUED = 3;
        public static final int STATUS_DOWNLOAD = 4;
        public static final int STATUS_UPLOAD = 6;
        private final String peerDownloadStr = "Downloading from %d of %d peers — ↓ %s ↑ %s";
        private final String peerUploadStr = "Seeding to %d of %d peers — ↑ %s";
        private final String peerPausedStr = "Paused";
        private final String peerCheckedStr = "Verifying local data";
        private final String peerQueuedStr = "Queued";
        private HBox graphicBox;
        private Label peersAndSpeed, name, fileProgress;
        private ProgressBar pb;
        private TorrentField torrent;
        private String fileDownloadStr = "%s of %s — %s remaining";
        private String fileUploadStr = "%s, uploaded %s (Ratio %.2f)";
        private String fileCheckedStr = "%s of %s";

        public void setFields(TorrentField torrent) {
            this.torrent = torrent;

            name = createNameLabel();
            peersAndSpeed = createPeersAndSpeed();
            peersAndSpeed.setId("peers");
            fileProgress = createFileProgress();
            fileProgress.setId("file");

            HBox hbox = new HBox(10);
            pb = createProgressBar();
            Label progressLabel = new Label();
            hbox.getChildren().addAll(pb, progressLabel);

            VBox vbox = new VBox();
            vbox.getChildren().addAll(name, peersAndSpeed, hbox, fileProgress);

            ImageView image = new ImageView();
            String extension = torrent.getName().substring(torrent.getName().lastIndexOf(".") + 1).toLowerCase();
            InputStream stream;
            stream = getClass().getResourceAsStream("/filetypes/" + extension + ".png");
            if (stream == null)
                stream = getClass().getResourceAsStream("/filetypes/folder.png");
            if (stream != null)
                image.setImage(new Image(stream));

            graphicBox = new HBox(10);
            graphicBox.getChildren().addAll(image, vbox);

            pb.prefWidthProperty().bind(graphicBox.widthProperty().subtract(150));
            pb.progressProperty().setValue(torrent.getPercentDone());
            progressLabel.textProperty().bind(getPercent());
        }

        private SimpleStringProperty getPercent() {
            DoubleBinding percent = pb.progressProperty().multiply(100);
            String str = String.format("%.2f %%", percent.getValue());
            return new SimpleStringProperty(str);
        }

        private Label createNameLabel() {
            Label label = new Label();
            label.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
            label.setText(torrent.getName());
            return label;
        }

        private ProgressBar createProgressBar() {
            pb = new ProgressBar();
            if (torrent.getError() != 0) {
                pb.setStyle("-fx-accent: red;");
                return pb;
            }

            switch (torrent.getStatus()) {
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

            return pb;
        }

        private Label createPeersAndSpeed() {
            if (torrent.getError() != 0) {
                Label label = new Label(torrent.getErrorString());
                label.setTextFill(Color.RED);
                label.getStyleClass().add("custom-font");
                return label;
            }

            String status = "";
            switch (torrent.getStatus()) {
                case STATUS_DOWNLOAD:
                    status = String.format(peerDownloadStr, torrent.getPeersSendingToUs(), torrent.getPeersConnected(),
                            Helpers.humanizeSpeed(torrent.getRateDownload()),
                            Helpers.humanizeSpeed(torrent.getRateUpload()));
                    break;

                case STATUS_UPLOAD:
                    status = String.format(peerUploadStr, torrent.getPeersGettingFromUs(), torrent.getPeersConnected(),
                            Helpers.humanizeSpeed(torrent.getRateUpload()));
                    break;

                case STATUS_PAUSED:
                    status = peerPausedStr;
                    break;

                case STATUS_CHECKED:
                    status = peerCheckedStr;
                    break;

                case STATUS_QUEUED:
                    status = peerQueuedStr;
                    break;
            }
            Label label = new Label(status);
            label.getStyleClass().add("custom-font");
            label.setTextFill(Color.GRAY);
            return label;
        }

        private Label createFileProgress() {
            String str = "";
            String size = Helpers.humanizeSize((long) (torrent.getSizeWhenDone() * torrent.getPercentDone()));
            switch (torrent.getStatus()) {
                case STATUS_DOWNLOAD:
                    str = String.format(fileDownloadStr, size,
                            Helpers.humanizeSize(torrent.getSizeWhenDone()),
                            Helpers.humanizeTime(torrent.getEta()));
                    break;

                case STATUS_UPLOAD:
                    str = String.format(fileUploadStr, size,
                            Helpers.humanizeSize((long) (torrent.getSizeWhenDone() * torrent.getUploadRatio())),
                            torrent.getUploadRatio());
                    break;

                case STATUS_CHECKED:
                    str = String.format(fileCheckedStr, size, Helpers.humanizeSize(torrent.getSizeWhenDone()));
                    break;
            }

            Label label = new Label(str);
            label.getStyleClass().add("custom-font");
            label.setTextFill(Color.GRAY);
            return label;
        }

        public HBox getBox() {
            return graphicBox;
        }
    }
}
