package com.sergeybochkov.transmissionremote;

import com.jcabi.log.Logger;
import com.sergeybochkov.transmissionremote.fxutil.Target;
import com.sergeybochkov.transmissionremote.model.Peer;
import com.sergeybochkov.transmissionremote.model.TorResponse;
import com.sergeybochkov.transmissionremote.model.TorWrap;
import com.sergeybochkov.transmissionremote.scheduled.InfoSchedule;
import cordelia.client.TrClient;
import cordelia.rpc.TorrentGet;
import cordelia.rpc.TorrentSet;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Info implements Target {

    private final Stage stage;

    private final ObservableList<TorWrap> files = FXCollections.observableArrayList();
    private final ObservableList<Peer> peers = FXCollections.observableArrayList();

    @FXML
    private Label nameLabel;
    @FXML
    private TableView<Peer> peerView;
    @FXML
    private TreeTableView<TorWrap> fileView;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private CheckMenuItem wantedItem;
    @FXML
    private RadioMenuItem lowItem, normalItem, highItem;

    private TrClient client;
    private Integer id;
    private InfoSchedule infoSchedule;

    public Info(Stage stage, AppProperties props) {
        this.stage = stage;
        this.files.addListener((ListChangeListener<TorWrap>) change -> {
            for (TorWrap wrap : change.getList())
                setItem(wrap, fileView.getRoot());
        });
    }

    public void withClient(TrClient client, Object id) {
        this.client = client;
        this.id = (Integer) id;
        try {
            nameLabel.setText(
                    client.post(
                            new TorrentGet(
                                    Collections.singletonList("name"),
                                    id
                            ),
                            TorResponse.class
                    ).name());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        infoSchedule = new InfoSchedule(this.client, this.id);
        infoSchedule.setOnSucceeded(e -> {
            TorResponse rsp = (TorResponse) e.getSource().getValue();
            peers.clear();
            peers.addAll(rsp.peers());
            files.setAll(rsp.files());
        });
        infoSchedule.start();
    }

    @Override
    public void init() {
        stage.setOnCloseRequest(e -> {
            if (infoSchedule != null)
                infoSchedule.cancel();
            files.clear();
            peers.clear();
            fileView.setRoot(new TreeItem<>());
        });
        this.peerView.itemsProperty().bind(new SimpleListProperty<>(peers));
        this.peerView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.fileView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.contextMenu.setOnShowing(event -> {
            TorWrap wrap = fileView.getSelectionModel().getSelectedItem().getValue();
            wantedItem.selectedProperty().setValue(wrap.wanted().getValue());
            switch (wrap.priority()) {
                case -1:
                    lowItem.setSelected(true);
                    break;
                case 0:
                    normalItem.setSelected(true);
                    break;
                case 1:
                    highItem.setSelected(true);
                    break;
            }
        });
    }

    private void setItem(TorWrap item, TreeItem<TorWrap> parent) {
        String[] parts = item.name().split("/");
        String filename = parts[parts.length - 1];
        TreeItem<TorWrap> parentRef = parent;
        for (String part : parts) {
            boolean finded = false;
            for (TreeItem<TorWrap> it : parentRef.getChildren()) {
                if (it.getValue().name().equals(part)) {
                    parentRef = it;
                    finded = true;
                    break;
                }
            }
            if (!finded) {
                TreeItem<TorWrap> node = part.equals(filename) ?
                        new TreeItem<>(item) :
                        new TreeItem<>();
                node.setExpanded(true);
                parentRef.getChildren().add(node);
            } else {
                if (parentRef.isLeaf())
                    parentRef.setValue(item);
            }
        }
    }

    private List<Integer> selected() {
        return fileView.getSelectionModel().getSelectedItems()
                .stream()
                .map(e -> e.getValue().id())
                .collect(Collectors.toList());
    }

    @FXML
    private void lowPriority() {
        Map<String, Object> map = new HashMap<>();
        map.put("ids", Collections.singletonList(id));
        map.put("priority-low", selected());
        try {
            client.post(new TorrentSet(map));
        } catch (IOException ex) {
            Logger.warn(this, "%s", ex);
        }
    }

    @FXML
    private void normalPriority() {
        Map<String, Object> map = new HashMap<>();
        map.put("ids", Collections.singletonList(id));
        map.put("priority-normal", selected());
        try {
            client.post(new TorrentSet(map));
        } catch (IOException ex) {
            Logger.warn(this, "%s", ex);
        }
    }

    @FXML
    private void highPriority() {
        Map<String, Object> map = new HashMap<>();
        map.put("ids", Collections.singletonList(id));
        map.put("priority-high", selected());
        try {
            client.post(new TorrentSet(map));
        } catch (IOException ex) {
            Logger.warn(this, "%s", ex);
        }
    }
}
