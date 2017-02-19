package com.sergeybochkov.transmissionremote;

import com.jcabi.log.Logger;
import com.sergeybochkov.transmissionremote.fxutil.MainTarget;
import com.sergeybochkov.transmissionremote.fxutil.View;
import com.sergeybochkov.transmissionremote.model.Speed;
import com.sergeybochkov.transmissionremote.model.Torrent;
import com.sergeybochkov.transmissionremote.model.TorrentComparator;
import com.sergeybochkov.transmissionremote.scheduled.FreeSpaceSchedule;
import com.sergeybochkov.transmissionremote.scheduled.SessionSchedule;
import com.sergeybochkov.transmissionremote.scheduled.TorrentSchedule;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
import cordelia.rpc.SessionGet;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Main implements MainTarget {

    private final Map<String, View> views = new HashMap<>();
    private final Stage stage;
    private final AppProperties props;

    private final Map<String, Object> session = new HashMap<>();
    private final ObservableList<Torrent> items = FXCollections.observableArrayList();

    @FXML
    private Button startAllButton, stopAllButton, trashButton, infoButton;
    @FXML
    private Label freeSpace, downSpeed, upSpeed, rating;
    @FXML
    private ListView<Torrent> torrents;

    private TrClient client;
    private TorrentSchedule torrentSchedule;
    private SessionSchedule sessionSchedule;
    private FreeSpaceSchedule freeSpaceSchedule;

    public Main(Stage stage, AppProperties props) {
        this.stage = stage;
        this.props = props;
    }

    @Override
    public void init() {
        stage.setTitle(TransmissionRemote.APP_NAME);
        stage.setMinWidth(TransmissionRemote.MIN_WIDTH);
        stage.setMinHeight(TransmissionRemote.MIN_HEIGHT);
        stage.setHeight(props.height());
        stage.setWidth(props.width());
        stage.getIcons().add(new Image(Main.class.getResourceAsStream(TransmissionRemote.LOGO)));
        torrents.itemsProperty().bind(new SimpleListProperty<>(items));
        torrents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        torrents.setOnMouseClicked((MouseEvent ev) -> {
            EventTarget target = ev.getTarget();
            if (target instanceof ListCell) {
                ListCell cell = (ListCell) target;
                if (cell.getGraphic() == null)
                    torrents.getSelectionModel().select(-1);
            }
            ev.consume();
        });
        torrents.setOnDragOver((DragEvent ev) -> {
            ev.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            ev.consume();
        });
        torrents.setOnDragDropped((DragEvent ev) -> {
            Dragboard db = ev.getDragboard();
            List<File> files = db.getFiles();
            if (files.size() == 0) {
                // add by url  db.getUrl()
            } else {
                // add by file
            }
            ev.setDropCompleted(true);
            ev.consume();
        });
        trashButton.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        infoButton.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
    }

    @Override
    public void withViews(Map<String, View> views) {
        this.views.putAll(views);
        this.views
                .get("session")
                .target(Session.class)
                .callback(this::restart);
    }

    public void start() {
        controlsActive(false);
        client = new TrClient(props.uri());
        try {
            session.putAll(
                    client.post(new SessionGet(), TrResponse.class).arguments());
            // TORRENT-UPDATE
            torrentSchedule = new TorrentSchedule(client);
            torrentSchedule.setOnSucceeded(event -> {
                List<Integer> indexes = torrents
                        .getSelectionModel()
                        .getSelectedIndices()
                        .stream()
                        .collect(Collectors.toList());
                List list = (List) event.getSource().getValue();
                items.clear();
                for (Object obj : list) {
                    if (obj instanceof Torrent)
                        items.add((Torrent) obj);
                }
                items.sort(new TorrentComparator());
                indexes.forEach(i -> torrents.getSelectionModel().select(i));
                long completed = torrents.getItems().stream().filter(Torrent::completed).count();
                com.apple.eawt.Application.getApplication().setDockIconBadge(completed > 0 ? String.valueOf(completed) : "");
            });
            torrentSchedule.setOnFailed(event ->
                    Logger.warn(this, "%s" , event.getSource().getException()));
            torrentSchedule.start();
            // SESSION-UPDATE
            sessionSchedule = new SessionSchedule(client);
            sessionSchedule.setOnSucceeded(event -> {
                Map map = (Map) event.getSource().getValue();
                Logger.debug(this, "%s", map);
                Map cumul = (Map) map.get("cumulative-stats");
                rating.setText(String.format("%.2f",
                        (double) cumul.get("uploadedBytes") / (double) cumul.get("downloadedBytes")));
                upSpeed.setText(new Speed((double) map.get("uploadSpeed")).toString());
                downSpeed.setText(new Speed((double) map.get("downloadSpeed")).toString());
            });
            sessionSchedule.start();
            // FREE-SPACE
            freeSpaceSchedule = new FreeSpaceSchedule(client, (String) session.get("download-dir"));
            freeSpaceSchedule.setOnSucceeded(event ->
                    freeSpace.setText(event.getSource().getValue().toString()));
            freeSpaceSchedule.start();
            controlsActive(true);
        } catch (IOException ex) {
            session.clear();
            session();
        }
    }

    public void restart() {
        if (freeSpaceSchedule != null) {
            freeSpaceSchedule.cancel();
            freeSpace.setText("");
        }
        if (sessionSchedule != null) {
            sessionSchedule.cancel();
            rating.setText("");
            upSpeed.setText("");
            downSpeed.setText("");
        }
        if (torrentSchedule != null) {
            torrentSchedule.cancel();
            items.clear();
        }
        start();
    }

    public void controlsActive(boolean active) {

    }

    @FXML
    private void about() {
        this.views
                .get("about")
                .stage()
                .show();
    }

    @FXML
    private void session() {
        this.views
                .get("session")
                .stage()
                .show();
    }
}
