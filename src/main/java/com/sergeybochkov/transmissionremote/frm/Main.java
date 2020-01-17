package com.sergeybochkov.transmissionremote.frm;

import com.sergeybochkov.transmissionremote.AppProperties;
import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.fxutil.MainTarget;
import com.sergeybochkov.transmissionremote.fxutil.View;
import com.sergeybochkov.transmissionremote.model.*;
import com.sergeybochkov.transmissionremote.scheduled.FreeSpaceSchedule;
import com.sergeybochkov.transmissionremote.scheduled.SessionSchedule;
import com.sergeybochkov.transmissionremote.scheduled.TorrentSchedule;
import com.sergeybochkov.transmissionremote.ui.IconLabel;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
import cordelia.rpc.SessionGet;
import cordelia.rpc.SessionSet;
import cordelia.rpc.Torrent;
import cordelia.rpc.TorrentRemove;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public final class Main implements MainTarget {

    private final Map<String, View> views = new HashMap<>();
    private final Stage stage;
    private final AppProperties props;

    private final Map<String, Object> session = new HashMap<>();
    private final ObservableList<Tor> items = FXCollections.observableArrayList();

    @FXML
    private MenuItem startItem;
    @FXML
    private MenuItem stopItem;
    @FXML
    private MenuItem infoItem;
    @FXML
    private MenuItem reannounceItem;
    @FXML
    private MenuItem trashItem;
    @FXML
    private MenuItem deleteItem;
    @FXML
    private MenuItem startContextItem;
    @FXML
    private MenuItem stopContextItem;
    @FXML
    private MenuItem reannounceContextItem;
    @FXML
    private MenuItem infoContextItem;
    @FXML
    private MenuItem trashContextItem;
    @FXML
    private MenuItem deleteContextItem;
    @FXML
    private Button trashButton;
    @FXML
    private Button infoButton;
    @FXML
    private ToggleButton speedLimitButton;
    @FXML
    private Label freeSpace;
    @FXML
    private Label downSpeed;
    @FXML
    private Label upSpeed;
    @FXML
    private Label rating;
    @FXML
    private ListView<Tor> torrents;

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
        stage.getIcons().add(new Image(getClass().getResourceAsStream(TransmissionRemote.LOGO)));
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
        torrents.setOnDragOver(this::dragOver);
        torrents.setOnDragDropped(this::dragDrop);
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
            speedLimitButton.setGraphic(new IconLabel(speedLimitButton.isSelected() ?
                    TransmissionRemote.ICON_ANCHOR :
                    TransmissionRemote.ICON_ROCKET));
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
                .callback(object -> {
                    if (object instanceof TrSource)
                        new TrSourceTrash((TrSource) object).add(client);
                });
    }

    public void start() {
        client = new TrClient(props.uri());
        try {
            session.putAll(
                    client.post(new SessionGet(), TrResponse.class).arguments()
            );
            // TORRENT-UPDATE
            torrentSchedule = new TorrentSchedule(client);
            torrentSchedule.setOnSucceeded(event -> {
                List<Integer> indexes = new ArrayList<>(torrents.getSelectionModel().getSelectedIndices());
                List list = (List) event.getSource().getValue();
                items.clear();
                for (Object obj : list) {
                    if (obj instanceof Tor)
                        items.add((Tor) obj);
                }
                items.sort(Comparator.comparingInt(Tor::id));
                indexes.forEach(i -> torrents.getSelectionModel().select(i));
                long completed = torrents.getItems().stream().filter(Tor::completed).count();
                Taskbar.getTaskbar().setIconBadge(
                        completed > 0 ? String.valueOf(completed) : ""
                );
            });
            torrentSchedule.start();
            // SESSION-UPDATE
            sessionSchedule = new SessionSchedule(client);
            sessionSchedule.setOnSucceeded(event -> {
                Map map = (Map) event.getSource().getValue();
                stage.setTitle(String.format("%s - [%s] - %s",
                        TransmissionRemote.APP_NAME,
                        props.server(),
                        map.get("version")));
                upSpeed.setText(new HumanSpeed((Double) map.get("uploadSpeed")).toString());
                downSpeed.setText(new HumanSpeed((Double) map.get("downloadSpeed")).toString());
                speedLimitButton.setSelected((Boolean) map.get("alt-speed-enabled"));
                Map cumul = (Map) map.get("cumulative-stats");
                rating.setText(String.format("%.2f",
                        (Double) cumul.get("uploadedBytes") / (Double) cumul.get("downloadedBytes")));
            });
            sessionSchedule.start();
            // FREE-SPACE
            freeSpaceSchedule = new FreeSpaceSchedule(client, session);
            freeSpaceSchedule.setOnSucceeded(event ->
                    freeSpace.setText(event.getSource().getValue().toString()));
            freeSpaceSchedule.start();
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

    private void alert(Exception ex) {
        new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
    }

    private Object[] allIds() {
        return torrents.getItems()
                .stream()
                .map(Tor::id)
                .toArray();
    }

    private Object[] selectedIds() {
        return torrents.getSelectionModel().getSelectedItems()
                .stream()
                .map(Tor::id)
                .toArray();
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
            client.post(new TorrentRemove(false, selectedIds()));
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
        this.views
                .get("info")
                .target(Info.class)
                .withClient(client, selectedIds()[0]);
        this.views
                .get("info")
                .stage()
                .show();
    }

    @FXML
    private void close() {
        Platform.exit();
    }

    private void dragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        event.consume();
    }

    public void dragDrop(DragEvent event) {
        Dragboard db = event.getDragboard();
        List<File> files = db.getFiles();
        if (files != null && !files.isEmpty())
            try {
                new TrSourceTrash(
                        new TrSourceFile(files, session)
                ).add(client);
            } catch (IOException ex) {
                alert(ex);
            }
        if (db.getUrl() != null && !db.getUrl().isEmpty()) {
            try {
                new TrSourceTrash(
                        new TrSourceUrl(db.getUrl(), session)
                ).add(client);
            } catch (IOException ex) {
                alert(ex);
            }
        }
        event.setDropCompleted(true);
        event.consume();
    }
}