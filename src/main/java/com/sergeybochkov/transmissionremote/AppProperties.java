package com.sergeybochkov.transmissionremote;

import com.google.gson.annotations.SerializedName;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AppProperties {

    private static final Pattern FULL_URL = Pattern.compile("(?<protocol>https?://)" +
            "(?<username>.*?):(?<password>.*?)@(?<host>.*?):(?<port>\\d+)/(?<path>.*)");
    private static final Pattern SHORT_URL = Pattern.compile("(?<protocol>https?://)(?<host>.*?):(?<port>\\d+)/(?<path>.*)");

    private Client client;
    private Stage stage;
    private Dirs dirs;

    public AppProperties() {
        this.client = new Client();
        this.stage = new Stage();
        this.dirs = new Dirs();
    }

    private class Client {
        private String url;
    }

    private class Stage {
        private Integer width;
        private Integer height;
    }

    private class Dirs {
        @SerializedName("last-destination")
        private String lastDestination;
        @SerializedName("last-open-path")
        private String lastOpenPath;
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
