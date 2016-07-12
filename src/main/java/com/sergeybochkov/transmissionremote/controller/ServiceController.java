package com.sergeybochkov.transmissionremote.controller;

import com.sergeybochkov.transmissionremote.helpers.Helpers;
import com.sergeybochkov.transmissionremote.helpers.Settings;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.*;
import com.sergeybochkov.transmissionremote.model.rpc.methods.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceController {

    private static Client client;

    private static SessionArguments session;
    private static SessionStatArguments stat;

    private double intervalFactor = 1;

    private SimpleStringProperty server = new SimpleStringProperty();
    private SimpleStringProperty version = new SimpleStringProperty();
    private SimpleStringProperty uploadSpeed = new SimpleStringProperty();
    private SimpleStringProperty downloadSpeed = new SimpleStringProperty();
    private SimpleStringProperty rating = new SimpleStringProperty();
    private SimpleStringProperty freeSpace = new SimpleStringProperty();
    private ObservableList<TorrentField> torrents = FXCollections.observableArrayList();
    private SimpleStringProperty titleString = new SimpleStringProperty();

    private ArrayList<ScheduledService> pool = new ArrayList<>(3);

    public ServiceController(ListView<TorrentField> listView) {
        client = Client.getInstance();
        session = (SessionArguments) client.send(new SessionGet()).getArguments();
        stat = (SessionStatArguments) client.send(new SessionStats()).getArguments();

        pool.add(createSessionService());
        pool.add(createFreeSpaceService());
        pool.add(createListUpdateService(listView));
        startServices();
    }

    private SessionUpdateService createSessionService() {
        SessionUpdateService service = new SessionUpdateService();
        service.setOnSucceeded(event -> {
            server.setValue(client.getServer());
            version.setValue(session.getVersion());
            uploadSpeed.setValue(Helpers.humanizeSpeed(stat.getUploadSpeed()));
            downloadSpeed.setValue(Helpers.humanizeSpeed(stat.getDownloadSpeed()));
            rating.setValue(String.format("%.2f", stat.getCumulativeStats().getRating()));
            titleString.setValue(String.format(" - [%s] - %s", server.getValue(), version.getValue()));
        });
        service.setOnFailed(event -> {
            server.setValue(Settings.emptyString);
            version.setValue(Settings.emptyString);
            uploadSpeed.setValue(Settings.emptyString);
            downloadSpeed.setValue(Settings.emptyString);
            rating.setValue(Settings.emptyString);
            titleString.setValue(Settings.emptyString);
        });
        return service;
    }

    private FreeSpaceUpdateService createFreeSpaceService() {
        FreeSpaceUpdateService service = new FreeSpaceUpdateService();
        service.setOnSucceeded(event -> freeSpace.setValue(service.getValue()));
        service.setOnFailed(event -> freeSpace.setValue(Settings.emptyString));
        return service;
    }

    private ListUpdateService createListUpdateService(ListView<TorrentField> listView) {

        Comparator<TorrentField> comparator = (tf1, tf2) -> {
            if (tf1.getId() > tf2.getId()) return 1;
            if (tf1.getId() < tf2.getId()) return -1;
            return 0;
        };

        ListUpdateService service = new ListUpdateService();
        service.setOnSucceeded(event -> {
            List<Integer> indexes = listView.getSelectionModel().getSelectedIndices().stream().collect(Collectors.toList());
            torrents.setAll(service.getValue());
            torrents.sort(comparator);
            indexes.forEach(e -> listView.getSelectionModel().select((int) e));

            long completed = torrents.stream().filter(tf -> tf.getPercentDone() >= 1.0).count();
            com.apple.eawt.Application.getApplication().setDockIconBadge(completed > 0 ? String.valueOf(completed) : null);
        });
        service.setOnFailed(event -> torrents.clear());
        return service;
    }

    private void startServices() {
        pool.forEach(ScheduledService::start);
    }

    public Client getClient() {
        return client;
    }

    public SessionArguments getSession() {
        return session;
    }

    public ObservableValue<String> getVersion() {
        return version;
    }

    public ObservableValue<String> getDownloadSpeed() {
        return downloadSpeed;
    }

    public ObservableValue<String> getUploadSpeed() {
        return uploadSpeed;
    }

    public ObservableValue<String> getRating() {
        return rating;
    }

    public ObservableValue<String> getFreeSpace() {
        return freeSpace;
    }

    public ObservableList<TorrentField> getTorrents() {
        return torrents;
    }

    public ObservableValue<String> getTitle() { return titleString; }

    public ObservableValue<String> getServer() {
        return server;
    }

    public String getDownloadDir() {
        return session.getDownloadDir();
    }

    public void addTorrentByUrl(String url) {
        client.send(new TorrentAdd().byUrl(url));
    }

    public void addTorrentByUrl(String url, String dir) {
        client.send(new TorrentAdd().setDownloadDir(dir).byUrl(url));
    }

    public void addTorrentByMetaInfo(String metainfo) {
        client.send(new TorrentAdd().byFile(metainfo));
    }

    public void addTorrentByMetaInfo(String metainfo, String dir) {
        client.send(new TorrentAdd().setDownloadDir(dir).byFile(metainfo));
    }

    public void setAltSpeedEnable(boolean enable) {
        client.send(new SessionSet().setAltSpeedEnabled(enable));
    }

    public void startTorrents(List<Integer> ids) {
        client.send(new TorrentStart(ids));
    }

    public void stopTorrents(List<Integer> ids) {
        client.send(new TorrentStop(ids));
    }

    public void reannounceTorrents(List<Integer> ids) {
        client.send(new TorrentReannounce(ids));
    }

    public void deleteTorrent(List<Integer> ids, boolean withFiles) {
        client.send(new TorrentRemove(ids, withFiles));
    }

    public void setWanted(Boolean wanted, List<Integer> id, List<Integer> ids) {
        if (wanted)
            client.send(new TorrentSet(id).setWanted(ids));
        else
            client.send(new TorrentSet(id).setUnwanted(ids));
    }

    public void setPriority(Integer priority, List<Integer> id, List<Integer> ids) {
        TorrentSet ts = new TorrentSet(id);
        switch (priority) {
            case -1:
                client.send(ts.setLowPriority(ids));
                break;
            case 0:
                client.send(ts.setNormalPriority(ids));
                break;
            case 1:
                client.send(ts.setHighPriority(ids));
                break;
        }
    }

    public void changeInterval(double value) {
        intervalFactor = value;
        pool.parallelStream().forEach(serv -> serv.setPeriod(serv.getPeriod().multiply(intervalFactor)));
    }

    public void resetInterval() {
        pool.parallelStream().forEach(serv -> serv.setPeriod(serv.getPeriod().divide(intervalFactor)));
        intervalFactor = 1;
    }

    public void terminateServices() {
        pool.forEach(ScheduledService::cancel);
        pool.clear();
    }

    private static class FreeSpaceUpdateService extends ScheduledService<String> {

        public FreeSpaceUpdateService() {
            setPeriod(new Duration(Settings.freeSpaceUpdateInterval));
        }

        @Override
        protected Task<String> createTask() {
            return new Task<String>() {
                @Override
                protected String call() throws Exception {
                    FreeSpaceArguments args = (FreeSpaceArguments) client.send(new FreeSpace(session.getDownloadDir())).getArguments();
                    return Helpers.humanizeSize(args.getSizeBytes());
                }
            };
        }
    }

    private static class SessionUpdateService extends ScheduledService<Object> {

        public SessionUpdateService() {
            setPeriod(new Duration(Settings.updateSessionInterval));
        }

        @Override
        protected Task<Object> createTask() {
            return new Task<Object>() {
                @Override
                protected Object call() throws Exception {
                    session = (SessionArguments) client.send(new SessionGet()).getArguments();
                    stat = (SessionStatArguments) client.send(new SessionStats()).getArguments();
                    return null;
                }
            };
        }
    }

    private static class ListUpdateService extends ScheduledService<ObservableList<TorrentField>> {

        TorrentGetArguments args;
        ObservableList<TorrentField> res = FXCollections.observableArrayList();

        public ListUpdateService() {
            setPeriod(new Duration(Settings.updateListInterval));
        }

        @Override
        protected Task<ObservableList<TorrentField>> createTask() {
            return new Task<ObservableList<TorrentField>>() {
                @Override
                protected ObservableList<TorrentField> call() throws Exception {
                    List<String> fields = Arrays.asList("id", "name", "percentDone", "peersSendingToUs",
                            "peersGettingFromUs", "sizeWhenDone", "peersConnected",
                            "status", "rateDownload", "rateUpload", "uploadRatio",
                            "eta", "error", "errorString");
                    args = (TorrentGetArguments) client.send(new TorrentGet().setFields(fields)).getArguments();
                    res.clear();
                    res.addAll(args.getFields());
                    return res;
                }
            };
        }
    }
}

