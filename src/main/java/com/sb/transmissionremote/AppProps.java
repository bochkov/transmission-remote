package com.sb.transmissionremote;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AppProps extends Properties {

    public static final String LAST_DESTINATION = "app.dirs.last-destination";
    public static final String LAST_OPEN_PATH = "app.dirs.last-open-path";
    public static final String WINDOW_WIDTH = "app.stage.width";
    public static final String WINDOW_HEIGHT = "app.stage.height";
    public static final String TRANSMISSION_URL = "app.transmission.url";
    public static final String TRANSMISSION_USER = "app.transmission.user";
    public static final String TRANSMISSION_PASS = "app.transmission.password";

    private static final File SETTING_DIR = new File(System.getProperty("user.home"), ".transmission-remote");
    private static final String SETTING_FILE = "settings.properties";
    private static final Pattern SHORT_URL = Pattern.compile("(?<protocol>https?://)(?<host>.*?):(?<port>\\d+)/(?<path>.*)");

    private static AppProps instance;

    private AppProps() {
        //
    }

    public static AppProps get() {
        if (instance == null) {
            instance = new AppProps();
        }
        return instance;
    }

    public void load() {
        File propsFile = new File(SETTING_DIR, SETTING_FILE);
        if (!propsFile.getParentFile().exists() && !propsFile.getParentFile().mkdirs()) {
            LOG.warn("can't create settings dir");
        }
        if (propsFile.exists()) {
            try (FileReader reader = new FileReader(propsFile)) {
                load(reader);
                LOG.info("props loaded");
            } catch (IOException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }

    public void store() {
        try (FileWriter writer = new FileWriter(new File(SETTING_DIR, SETTING_FILE))) {
            store(writer, "");
            LOG.info("props stored");
        } catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }

    public static boolean isMacOs() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public static String serverUrl() {
        var m = SHORT_URL.matcher(AppProps.get(AppProps.TRANSMISSION_URL));
        return m.find() ?
                String.format("%s", m.group("host")) :
                "";
    }

    public static int getInt(String key, int def) {
        return Integer.parseInt(
                instance.getProperty(key, String.valueOf(def))
        );
    }

    public static String get(String key) {
        return instance.getProperty(key);
    }

    public static String get(String key, String def) {
        return instance.getProperty(key, def);
    }

    public static void putVal(String key, Object value) {
        instance.put(key, value);
    }
}
