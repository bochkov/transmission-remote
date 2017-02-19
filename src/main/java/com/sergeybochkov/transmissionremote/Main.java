package com.sergeybochkov.transmissionremote;

import com.sergeybochkov.transmissionremote.fxutil.MainTarget;
import com.sergeybochkov.transmissionremote.fxutil.View;
import com.sergeybochkov.transmissionremote.scheduled.FreeSpaceSchedule;
import cordelia.client.TrClient;
import cordelia.rpc.SessionGet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Main implements MainTarget {

    private final Map<String, View> views = new HashMap<>();
    private final Stage stage;
    private final AppProperties props;

    @FXML
    private Label freeSpace;

    private TrClient client;
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
    }

    @Override
    public void withViews(Map<String, View> views) {
        this.views.putAll(views);
        this.views
                .get("session")
                .target(Session.class)
                .callback(object -> {
                });
    }

    public void start() throws IOException {
        client = new TrClient(props.uri());
        try {
            client.post(new SessionGet());
        } catch (IOException ex) {
            session();
            return;
        }

        freeSpaceSchedule = new FreeSpaceSchedule(client);
        freeSpaceSchedule.setOnSucceeded(event -> {
            System.out.println(event);
        });
        freeSpaceSchedule.start();
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
