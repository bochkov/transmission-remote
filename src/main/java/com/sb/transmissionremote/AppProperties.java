package com.sb.transmissionremote;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public final class AppProperties {

    private static final File SETTING_DIR = new File(System.getProperty("user.home"), ".transmissionremote");
    private static final String SETTING_FILE = "settings.json";
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final Pattern FULL_URL = Pattern.compile("(?<protocol>https?://)(?<username>.*?):(?<password>.*?)@(?<host>.*?):(?<port>\\d+)/(?<path>.*)");
    private static final Pattern SHORT_URL = Pattern.compile("(?<protocol>https?://)(?<host>.*?):(?<port>\\d+)/(?<path>.*)");

    private final Client client = new Client();
    private final Stage stage = new Stage();
    private final Dirs dirs = new Dirs();

    @Data
    private static class Client {
        private String url;
    }

    @Data
    private static class Stage {
        private Integer width;
        private Integer height;
    }

    @Data
    private static class Dirs {
        @JsonProperty("last-destination")
        private String lastDestination;
        @JsonProperty("last-open-path")
        private String lastOpenPath;
    }

    private static AppProperties props;

    private AppProperties() {
        //
    }

    public static AppProperties get() {
        if (props == null)
            props = AppProperties.load();
        return props;
    }

    private static AppProperties load() {
        File propsFile = new File(SETTING_DIR, SETTING_FILE);
        if (!propsFile.getParentFile().exists() && !propsFile.getParentFile().mkdirs()) {
            LOG.warn("can't create settings dir");
        }
        if (propsFile.exists()) {
            try (FileReader reader = new FileReader(propsFile)) {
                LOG.info("props loaded");
                return JSON.readValue(reader, AppProperties.class);
            } catch (IOException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
        return new AppProperties();
    }

    public void store() {
        try (FileWriter writer = new FileWriter(new File(SETTING_DIR, SETTING_FILE))) {
            JSON.writeValue(writer, this);
            LOG.info("props stored");
        } catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }

    @JsonIgnore
    public boolean isMacOs() {
        return System.getProperty("os.name").contains("mac");
    }

    public String uri() {
        if (client.url == null)
            setUrl("http://127.0.0.1:9091/transmission/rpc", "username", "password");
        return client.url;
    }

    public String url() {
        Matcher m = FULL_URL.matcher(uri());
        if (m.find()) {
            return String.format("%s%s:%s/%s",
                    m.group("protocol"),
                    m.group("host"),
                    m.group("port"),
                    m.group("path"));
        } else
            return "";
    }

    public String username() {
        Matcher m = FULL_URL.matcher(uri());
        return m.find() ?
                m.group("username") :
                "";
    }

    public String password() {
        Matcher m = FULL_URL.matcher(uri());
        return m.find() ?
                m.group("password") :
                "";
    }

    public String server() {
        Matcher m = FULL_URL.matcher(uri());
        return m.find() ?
                String.format("%s", m.group("host")) :
                "";
    }

    public void setUrl(String url, String username, String password) {
        Matcher m = SHORT_URL.matcher(url);
        if (m.find()) {
            client.url = String.format("%s%s:%s@%s:%s/%s",
                    m.group("protocol"),
                    username,
                    password,
                    m.group("host"),
                    m.group("port"),
                    m.group("path"));
        }
    }

    public int width() {
        if (stage.width == null)
            setWidth(TransmissionRemote.MIN_WIDTH);
        return stage.width;
    }

    public void setWidth(double width) {
        stage.width = (int) width;
    }

    public int height() {
        if (stage.height == null)
            setHeight(TransmissionRemote.MIN_HEIGHT);
        return stage.height;
    }

    public void setHeight(double height) {
        stage.height = (int) height;
    }

    public String lastDestination() {
        if (dirs.lastDestination == null)
            setLastDestination("");
        return dirs.lastDestination;
    }

    public void setLastDestination(String path) {
        dirs.lastDestination = path;
    }

    public String lastOpenPath() {
        if (dirs.lastOpenPath == null)
            setLastOpenPath("");
        return dirs.lastOpenPath;
    }

    public void setLastOpenPath(String path) {
        dirs.lastOpenPath = path;
    }
}
