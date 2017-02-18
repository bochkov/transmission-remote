package com.sergeybochkov.transmissionremote.fxutil;

import com.sergeybochkov.transmissionremote.AppProperties;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class View {

    private final Stage stage;
    private final Target target;
    private final Map<String, View> views = new HashMap<>();

    public View(String location, AppProperties properties) throws Exception {
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
        loader.setControllerFactory(clz -> {
            try {
                return clz
                        .getConstructor(Stage.class, AppProperties.class)
                        .newInstance(stage, properties);
            }
            catch (Exception ex) {
                return null;
            }
        });
        stage.setScene(new Scene(loader.load()));
        target = loader.getController();
        target.init();
    }

    public View children(View... views) {
        for (View view : views) {
            view.stage().initOwner(stage);
            view.stage().initModality(Modality.APPLICATION_MODAL);
            view.stage().setTitle(stage.getTitle());
            view.stage().getIcons().addAll(stage.getIcons());
            this.views.put(view.target().getClass().getSimpleName().toLowerCase(), view);
        }
        if (target instanceof MainTarget)
            ((MainTarget) target).withViews(this.views);
        return this;
    }

    public <T> T target(Class<T> castClz) {
        return castClz.cast(target());
    }

    public Target target() {
        return target;
    }

    public Stage stage() {
        return stage;
    }
}
