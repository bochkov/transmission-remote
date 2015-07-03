package com.sergeybochkov.transmissionremote.helpers;

public class Properties {

    private Client client = new Client();
    private Stage stage = new Stage();
    private Directories dirs = new Directories();

    public Client getClient() {
        return client;
    }

    public String getClientHost() {
        return client.host;
    }

    public void setClientHost(String host) {
        client.host = host;
    }

    public Integer getClientPort() {
        return client.port;
    }

    public void setClientPort(Integer port) {
        client.port = port;
    }

    public String getClientUsername() {
        return client.username;
    }

    public void setClientUsername(String username) {
        client.username = username;
    }

    public String getClientPassword() {
        return client.password;
    }

    public void setClientPassword(String password) {
        client.password = password;
    }

    public Integer getStageHeight() {
        return stage.height;
    }

    public void setStageHeight(Integer height) {
        stage.height = height;
    }

    public Integer getStageWidth() {
        return stage.width;
    }

    public void setStageWidth(Integer width) {
        stage.width = width;
    }

    public void setLastDestination(String dest) {
        dirs.lastDestination = dest;
    }

    public String getLastDestination() {
        return dirs.lastDestination;
    }

    public void setLastOpenDialogPath(String path) {
        dirs.lastOpenDialogPath = path;
    }

    public String getLastOpenDialogPath() {
        return dirs.lastOpenDialogPath;
    }

    private class Client {
        private String host;
        private Integer port;
        private String username;
        private String password;
    }

    private class Stage {
        private Integer height;
        private Integer width;
    }

    private class Directories {
        private String lastDestination;
        private String lastOpenDialogPath;
    }
}
