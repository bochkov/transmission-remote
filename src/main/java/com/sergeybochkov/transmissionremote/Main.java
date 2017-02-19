package com.sergeybochkov.transmissionremote;

import com.jcabi.log.Logger;
import com.sergeybochkov.transmissionremote.fxutil.MainTarget;
import com.sergeybochkov.transmissionremote.fxutil.View;
import com.sergeybochkov.transmissionremote.model.Speed;
import com.sergeybochkov.transmissionremote.scheduled.FreeSpaceSchedule;
import com.sergeybochkov.transmissionremote.scheduled.SessionSchedule;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
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

    private final Map<String, Object> session = new HashMap<>();

    @FXML
    private Label freeSpace, downSpeed, upSpeed, rating;

    private TrClient client;
    private FreeSpaceSchedule freeSpaceSchedule;
    private SessionSchedule sessionSchedule;

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
                .callback(this::restart);
    }

    public void start() {
        controlsActive(false);
        client = new TrClient(props.uri());
        try {
            session.putAll(
                    client.post(new SessionGet(), TrResponse.class).arguments());
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
