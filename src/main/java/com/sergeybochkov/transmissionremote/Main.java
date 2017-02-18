package com.sergeybochkov.transmissionremote;

import com.sergeybochkov.transmissionremote.fxutil.MainTarget;
import com.sergeybochkov.transmissionremote.fxutil.View;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public final class Main implements MainTarget {

    private final Map<String, View> views = new HashMap<>();

    public Main(Stage stage, AppProperties props) {

    }

    @Override
    public void init() {

    }

    @Override
    public void withViews(Map<String, View> views) {
        this.views.putAll(views);
    }

    @FXML
    private void about() {
        this.views.get("about").stage().show();
    }
}
