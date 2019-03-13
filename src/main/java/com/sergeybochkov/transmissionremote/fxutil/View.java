package com.sergeybochkov.transmissionremote.fxutil;

import com.jcabi.log.Logger;
import com.sergeybochkov.transmissionremote.AppProperties;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class View {

    private final Stage stage;
    private final Target target;
    private final Map<String, View> views = new HashMap<>();

    public View(String location, AppProperties properties) throws IOException {
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
        loader.setControllerFactory(clz -> {
            Object obj = null;
            try {
                obj = clz
                        .getConstructor(Stage.class, AppProperties.class)
                        .newInstance(stage, properties);
            } catch (Exception ex) {
                Logger.debug(this, "Ctor with params Stage and AppProperties not found");
            }
            if (obj != null)
                return obj;
            try {
                obj = clz.getConstructor(Stage.class)
                        .newInstance(stage);
            } catch (Exception ex) {
                Logger.debug(this, "Ctor with params Stage not found");
            }
            return obj;
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
            view.stage().addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
                if (ev.getCode() == KeyCode.ESCAPE)
                    view.stage().close();
            });
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
