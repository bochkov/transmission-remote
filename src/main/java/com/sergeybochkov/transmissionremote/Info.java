package com.sergeybochkov.transmissionremote;

import com.sergeybochkov.transmissionremote.fxutil.Target;
import com.sergeybochkov.transmissionremote.model.Peer;
import com.sergeybochkov.transmissionremote.model.TorResponse;
import com.sergeybochkov.transmissionremote.model.TorWrap;
import com.sergeybochkov.transmissionremote.scheduled.InfoSchedule;
import cordelia.client.TrClient;
import cordelia.rpc.TorrentGet;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;

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

    private InfoSchedule infoSchedule;

    public Info(Stage stage, AppProperties props) {
        this.stage = stage;
        this.files.addListener((ListChangeListener<TorWrap>) change -> {
            for (TorWrap wrap : change.getList())
                setItem(wrap, fileView.getRoot());
        });
    }

    public void withClient(TrClient client, Object id) {
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
        infoSchedule = new InfoSchedule(client, (Integer) id);
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
        });
        this.peerView.itemsProperty().bind(new SimpleListProperty<>(peers));
        this.peerView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.fileView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
}
