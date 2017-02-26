package com.sergeybochkov.transmissionremote;

import com.sergeybochkov.transmissionremote.fxutil.Target;
import com.sergeybochkov.transmissionremote.model.Peer;
import com.sergeybochkov.transmissionremote.scheduled.FileInfoSchedule;
import com.sergeybochkov.transmissionremote.scheduled.PeerInfoSchedule;
import cordelia.client.TrClient;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public final class Info implements Target {

    private final Stage stage;
    private final AppProperties props;

    private final ObservableList<Peer> peers = FXCollections.observableArrayList();

    @FXML
    private TableView<Peer> peerView;

    private FileInfoSchedule fileSchedule;
    private PeerInfoSchedule peerSchedule;
    private Integer trId;
    private TrClient client;

    public Info(Stage stage, AppProperties props) {
        this.stage = stage;
        this.props = props;
    }

    public void withClient(TrClient client, Object id) {
        this.client = client;
        this.trId = (Integer) id;
        peerView.itemsProperty().bind(new SimpleListProperty<>(peers));
        peerSchedule = new PeerInfoSchedule(this.client, this.trId);
        peerSchedule.setOnSucceeded(e -> {
            List list = (List) e.getSource().getValue();
            peers.clear();
            for (Object obj : list) {
                if (obj instanceof Peer)
                    peers.add((Peer) obj);
            }
        });
        peerSchedule.start();
    }

    @Override
    public void init() {
        stage.setOnCloseRequest(e -> {
            if (fileSchedule != null)
                fileSchedule.cancel();
            if (peerSchedule != null)
                peerSchedule.cancel();
        });
    }
}
