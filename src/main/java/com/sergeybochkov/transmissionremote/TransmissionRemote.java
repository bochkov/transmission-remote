package com.sergeybochkov.transmissionremote;

import com.sergeybochkov.transmissionremote.fxutil.View;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;

public final class TransmissionRemote extends Application {

    public static final String APP_NAME = "Transmission Remote";
    public static final String LOGO = "/style/transmission-remote.png";
    public static final String FONT_AWESOME = "/style/fontawesome-webfont.woff";
    public static final String SETTING_DIR = System.getProperty("user.home") + File.separator + ".transmissionremote";
    public static final String SETTING_FILE = "settings.json";

    public static final int MIN_HEIGHT = 600;
    public static final int MIN_WIDTH = 450;

    public static final int updateSessionInterval = 3000;
    public static final int updateListInterval = 1500;
    public static final int freeSpaceUpdateInterval = 10000;
    public static final int peersUpdateInterval = 1000;
    public static final int filesUpdateInterval = 2000;

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

    private final View mainView;
    private final AppProperties props;

    public TransmissionRemote() throws Exception {
        Font.loadFont(getClass().getResource(FONT_AWESOME).toExternalForm(), 14);
        props = new AppProperties();
        mainView = new View(MAIN_LAYOUT, props)
                .children(
                        new View(SESSION_DIALOG_LAYOUT, props),
                        new View(ABOUT_DIALOG_LAYOUT, props));
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainView.stage().show();
        mainView.target(Main.class).start();
    }
}
