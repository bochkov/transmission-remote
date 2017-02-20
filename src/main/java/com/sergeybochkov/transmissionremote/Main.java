package com.sergeybochkov.transmissionremote;

import com.jcabi.log.Logger;
import com.sergeybochkov.transmissionremote.fxutil.MainTarget;
import com.sergeybochkov.transmissionremote.fxutil.View;
import com.sergeybochkov.transmissionremote.model.Speed;
import com.sergeybochkov.transmissionremote.model.Tr;
import com.sergeybochkov.transmissionremote.model.TrComparator;
import com.sergeybochkov.transmissionremote.scheduled.FreeSpaceSchedule;
import com.sergeybochkov.transmissionremote.scheduled.SessionSchedule;
import com.sergeybochkov.transmissionremote.scheduled.TorrentSchedule;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
import cordelia.rpc.*;
import javafx.application.Platform;
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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Main implements MainTarget {

    private final Map<String, View> views = new HashMap<>();
    private final Stage stage;
    private final AppProperties props;

    private final Map<String, Object> session = new HashMap<>();
    private final ObservableList<Tr> items = FXCollections.observableArrayList();

    @FXML
    private MenuItem startItem, stopItem, infoItem, reannounceItem, trashItem, deleteItem;
    @FXML
    private MenuItem startContextItem, stopContextItem, reannounceContextItem, infoContextItem, trashContextItem, deleteContextItem;
    @FXML
    private Button trashButton, infoButton;
    @FXML
    private ToggleButton speedLimitButton;
    @FXML
    private Label freeSpace, downSpeed, upSpeed, rating;
    @FXML
    private ListView<Tr> torrents;

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
                addTorrent(db.getUrl());
            } else {
                files.forEach(this::addTorrent);
            }
            ev.setDropCompleted(true);
            ev.consume();
        });
        startItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        stopItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        infoItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        reannounceItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        trashItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        deleteItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        startContextItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        stopContextItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        infoContextItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        reannounceContextItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        trashContextItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        deleteContextItem.disableProperty().bind(
                torrents.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
        speedLimitButton.selectedProperty().addListener((val, oldV, newV) -> {
            Label label = new Label(speedLimitButton.isSelected() ?
                    TransmissionRemote.ICON_ANCHOR :
                    TransmissionRemote.ICON_ROCKET);
            label.setStyle("-fx-font-size: 14px;");
            label.getStyleClass().add("icons");
            speedLimitButton.setGraphic(label);
            speedLimitButton.setTooltip(new Tooltip(
                    String.format("Now speed limit is %s",
                            speedLimitButton.isSelected() ? "ON" : "OFF")));
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
        this.views
                .get("add")
                .target(Add.class)
                .callback(objects -> {
                    if (objects.length == 3) {
                        List files = (List) objects[0];
                        String url = (String) objects[1];
                        String dir = (String) objects[2];
                        if (!files.isEmpty()) {
                            for (Object file : files)
                                if (file instanceof File)
                                    addTorrent((File) file, dir);
                        } else if (!url.isEmpty())
                            addTorrent(url, dir);
                    }
                });
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
                    if (obj instanceof Tr)
                        items.add((Tr) obj);
                }
                items.sort(new TrComparator());
                indexes.forEach(i -> torrents.getSelectionModel().select(i));
                long completed = torrents.getItems().stream().filter(Tr::completed).count();
                com.apple.eawt.Application.getApplication().setDockIconBadge(completed > 0 ? String.valueOf(completed) : "");
            });
            torrentSchedule.start();
            // SESSION-UPDATE
            sessionSchedule = new SessionSchedule(client);
            sessionSchedule.setOnSucceeded(event -> {
                Map map = (Map) event.getSource().getValue();
                Logger.info(this, "%s", map);
                stage.setTitle(String.format("%s - [%s] - %s",
                        TransmissionRemote.APP_NAME,
                        props.server(),
                        map.get("version")));
                upSpeed.setText(new Speed((Double) map.get("uploadSpeed")).toString());
                downSpeed.setText(new Speed((Double) map.get("downloadSpeed")).toString());
                speedLimitButton.setSelected((Boolean) map.get("alt-speed-enabled"));
                Map cumul = (Map) map.get("cumulative-stats");
                rating.setText(String.format("%.2f",
                        (Double) cumul.get("uploadedBytes") / (Double) cumul.get("downloadedBytes")));
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

    private void alert(Exception ex) {
        new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
    }

    private Object[] allIds() {
        return torrents.getItems()
                .stream()
                .map(Tr::id)
                .toArray();
    }

    private Object[] selectedIds() {
        return torrents.getSelectionModel().getSelectedItems()
                .stream()
                .map(Tr::id)
                .toArray();
    }

    private void addTorrent(String url) {
        addTorrent(url, (String) session.get("download-dir"));
    }

    private void addTorrent(String url, String directory) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("filename", url);
            map.put("download-dir", directory);
            client.post(new TorrentAdd(map));
        } catch (IOException ex) {
            alert(ex);
        }
    }

    private void addTorrent(File file) {
        addTorrent(file, (String) session.get("download-dir"));
    }

    private void addTorrent(File file, String directory) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("metainfo", Base64
                    .getEncoder()
                    .encodeToString(
                            FileUtils.readFileToByteArray(file)));
            map.put("download-dir", directory);
            client.post(new TorrentAdd(map));
        } catch (IOException ex) {
            alert(ex);
        }
    }

    @FXML
    private void startAll() {
        try {
            client.post(new Torrent(Torrent.Action.START, allIds()));
        } catch (IOException ex) {
            alert(ex);
        }
    }

    @FXML
    private void stopAll() {
        try {
            client.post(new Torrent(Torrent.Action.STOP, allIds()));
        } catch (IOException ex) {
            alert(ex);
        }
    }

    @FXML
    private void startTorrent() {
        try {
            client.post(new Torrent(Torrent.Action.START, selectedIds()));
        } catch (IOException ex) {
            alert(ex);
        }
    }

    @FXML
    private void stopTorrent() {
        try {
            client.post(new Torrent(Torrent.Action.STOP, selectedIds()));
        } catch (IOException ex) {
            alert(ex);
        }
    }

    @FXML
    private void reannounceTorrent() {
        try {
            client.post(new Torrent(Torrent.Action.REANNOUNCE, selectedIds()));
        } catch (IOException ex) {
            alert(ex);
        }
    }

    @FXML
    private void deleteTorrent() {
        try {
            client.post(new TorrentRemove(true, selectedIds()));
        } catch (IOException ex) {
            alert(ex);
        }
    }

    @FXML
    private void trashTorrent() {
        try {
            client.post(new TorrentRemove(selectedIds()));
        } catch (IOException ex) {
            alert(ex);
        }
    }

    @FXML
    private void turtle() {
        Map<String, Object> map = new HashMap<>();
        map.put("alt-speed-enabled", speedLimitButton.isSelected());
        try {
            client.post(new SessionSet(map));
        } catch (IOException ex) {
            alert(ex);
        }
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

    @FXML
    private void add() {
        this.views
                .get("add")
                .target(Add.class)
                .withClient(client);
        this.views
                .get("add")
                .stage()
                .show();
    }

    @FXML
    private void info() {

    }

    @FXML
    private void close() {
        Platform.exit();
    }
}
