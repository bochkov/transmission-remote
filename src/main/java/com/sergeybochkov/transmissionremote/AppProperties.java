package com.sergeybochkov.transmissionremote;

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
        private String lastDestination;
        private String lastOpenDialogPath;
    }

    public String uri() {
        return client.url;
    }

    public String url() {
        Matcher m = FULL_URL.matcher(client.url);
        if (m.find()) {
            return String.format("%s%s:%s/%s",
                    m.group("protocol"),
                    m.group("host"),
                    m.group("port"),
                    m.group("path"));
        } else
            return "";
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
        return stage.width;
    }

    public int height() {
        return stage.height;
    }
}
