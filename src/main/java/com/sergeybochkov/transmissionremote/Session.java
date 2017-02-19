package com.sergeybochkov.transmissionremote;

import com.sergeybochkov.transmissionremote.fxutil.ResultCallback;
import com.sergeybochkov.transmissionremote.fxutil.Target;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public final class Session implements Target, ResultCallback {

    private final Stage stage;
    private final AppProperties props;

    private Callback callback;

    public Session(Stage stage, AppProperties props) {
        this.stage = stage;
        this.props = props;
    }

    @Override
    public void init() {
        this.stage.setResizable(false);
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
