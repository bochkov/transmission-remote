package com.sergeybochkov.transmissionremote;

import com.sergeybochkov.transmissionremote.fxutil.ResultCallback;
import com.sergeybochkov.transmissionremote.fxutil.Target;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public final class Session implements Target, ResultCallback {

    private final Stage stage;
    private final AppProperties props;

    private Callback callback;

    @FXML
    private TextField server, username, password;
    @FXML
    private CheckBox auth;

    public Session(Stage stage, AppProperties props) {
        this.stage = stage;
        this.props = props;
    }

    @Override
    public void init() {
        this.stage.setWidth(TransmissionRemote.MIN_WIDTH - 50);
        this.stage.setResizable(false);
        this.username.disableProperty().bind(this.auth.selectedProperty().not());
        this.password.disableProperty().bind(this.auth.selectedProperty().not());
        // set props
        this.server.setText(props.url());
        this.auth.setSelected(!props.username().isEmpty() && !props.password().isEmpty());
        this.username.setText(props.username());
        this.password.setText(props.password());
    }

    @Override
    public Target callback(Callback callback) {
        this.callback = callback;
        return this;
    }

    @FXML
    public void onOk() throws IOException {
        callback.call(props);
        stage.close();
    }

    @FXML
    public void onCancel() {
        stage.close();
    }
}
