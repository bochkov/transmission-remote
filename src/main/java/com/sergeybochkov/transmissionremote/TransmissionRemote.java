package com.sergeybochkov.transmissionremote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sergeybochkov.transmissionremote.frm.Main;
import com.sergeybochkov.transmissionremote.fxutil.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class TransmissionRemote extends Application {

    public static final String APP_NAME = "Transmission Remote";
    public static final String LOGO = "/style/transmission-remote.png";
    public static final String FONT_AWESOME = "/style/fontawesome-webfont.woff";
    public static final String SETTING_DIR = System.getProperty("user.home") + File.separator + ".transmissionremote";
    public static final String SETTING_FILE = "settings.json";

    public static final int MIN_HEIGHT = 650;
    public static final int MIN_WIDTH = 500;

    public static final int SESSION_INTERVAL = 3000;
    public static final int TORRENT_INTERVAL = 1500;
    public static final int FREE_SPACE_INTERVAL = 5000;
    public static final int INFO_INTERVAL = 1000;

    public static final String ICON_GLOBE = "\uf0ac"; // session
    public static final String ICON_FOLDER_OPEN_O = "\uf115"; // open
    public static final String ICON_FOLDER_OPEN = "\uf114";
    public static final String ICON_SIGN_OUT = "\uf08b"; // exit
    public static final String ICON_REPLY_ALL = "\uf122"; // start all
    public static final String ICON_SQUARE = "\uf0c8"; // stop all
    public static final String ICON_REPLY = "\uf112"; // start
    public static final String ICON_SQUARE_O = "\uf096"; // stop
    public static final String ICON_INFO_CIRCLE = "\uf05a";  // info torrent
    public static final String ICON_REFRESH = "\uf021";  // reannounce torrent
    public static final String ICON_TRASH = "\uf014"; // delete
    public static final String ICON_BAN = "\uf05e"; // force delete
    public static final String ICON_FLASK = "\uf0c3"; // about
    public static final String ICON_ROCKET = "\uf135"; // speed limit
    public static final String ICON_ANCHOR = "\uf13d"; // alt speed limit
    public static final String ICON_LOCK = "\uf023"; // encryption
    public static final String ICON_CIRCLE = "\uf111"; // priority
    public static final String ICON_ARROW_UP = "\uf062"; // up
    public static final String ICON_ARROW_DOWN = "\uf063"; // down
    public static final String ICON_HDD = "\uf0a0"; // free space
    public static final String ICON_SIGNAL = "\uf012"; // rating
    public static final String ICON_ENVELOPE_O = "\uf003";
    public static final String ICON_LINK = "\uf0c1";

    private static final String MAIN_LAYOUT = "/ui/main.fxml";
    private static final String SESSION_DIALOG_LAYOUT = "/ui/session.fxml";
    private static final String ABOUT_DIALOG_LAYOUT = "/ui/about.fxml";
    private static final String ADD_DIALOG_LAYOUT = "/ui/add.fxml";
    private static final String INFO_DIALOG_LAYOUT = "/ui/info.fxml";

    private final View mainView;
    private final AppProperties props;

    public TransmissionRemote() throws IOException {
        Taskbar.getTaskbar().setIconImage(
                new ImageIcon(getClass().getResource(LOGO)).getImage()
        );
        Font.loadFont(getClass().getResource(FONT_AWESOME).toExternalForm(), 14);
        File propsFile = new File(SETTING_DIR, SETTING_FILE);
        if (!propsFile.getParentFile().exists() && !propsFile.getParentFile().mkdirs()) {
            throw new IOException(String.format("Cannot create user dir %s", propsFile.getParent()));
        }
        props = propsFile.exists() ?
                new Gson().fromJson(new FileReader(propsFile), AppProperties.class) :
                new AppProperties();
        mainView = new View(MAIN_LAYOUT, props)
                .children(
                        new View(ADD_DIALOG_LAYOUT, props),
                        new View(INFO_DIALOG_LAYOUT, props),
                        new View(SESSION_DIALOG_LAYOUT, props),
                        new View(ABOUT_DIALOG_LAYOUT, props)
                );
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainView.stage().show();
        Platform.runLater(() -> mainView.target(Main.class).start());
    }

    @Override
    public void stop() throws Exception {
        props.setWidth(mainView.stage().getWidth());
        props.setHeight(mainView.stage().getHeight());
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try (FileWriter writer = new FileWriter(new File(SETTING_DIR, SETTING_FILE))) {
            gson.toJson(props, writer);
        }
    }
}
