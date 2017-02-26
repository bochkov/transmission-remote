package com.sergeybochkov.transmissionremote;

import com.sergeybochkov.transmissionremote.fxutil.Target;
import cordelia.client.TrClient;
import javafx.stage.Stage;

public final class Info implements Target {

    private final Stage stage;
    private final AppProperties props;

    private TrClient client;

    public Info(Stage stage, AppProperties props) {
        this.stage = stage;
        this.props = props;
    }

    public void withClient(TrClient client) {
        this.client = client;
    }

    @Override
    public void init() {

    }
}
