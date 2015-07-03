package com.sergeybochkov.transmissionremote.controller;

import com.sergeybochkov.transmissionremote.helpers.Settings;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentField;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentGetArguments;
import com.sergeybochkov.transmissionremote.model.rpc.methods.TorrentGet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

public class InfoPeerController {

    private static Client client = Client.getInstance();
    private ObservableList<TorrentField.Peer> peers = FXCollections.observableArrayList();
    private PeerUpdateService service;

    @SuppressWarnings("unchecked")
    public InfoPeerController(ArrayList<Integer> ids, TableView<TorrentField.Peer> tableView) {

        service = new PeerUpdateService(ids);
        service.setOnSucceeded(event -> {
            // запоминаем выделение
            List<TorrentField.Peer> selected = tableView.getSelectionModel().getSelectedItems().stream().collect(Collectors.toList());

            peers.setAll(service.getValue());

            // восстанавливаем выделение
            for (TorrentField.Peer peer : selected)
                tableView.getItems()
                        .stream()
                        .filter(p -> p.getAddress().equals(peer.getAddress()))
                        .forEach(p -> tableView.getSelectionModel().select(p));
        });
    }

    public ObservableList<TorrentField.Peer> getObservablePeers() {
        return peers;
    }

    public PeerUpdateService getService() {
        return service;
    }

    private static class PeerUpdateService extends ScheduledService<ObservableList<TorrentField.Peer>> {

        ObservableList<TorrentField.Peer> peers = FXCollections.observableArrayList();
        private ArrayList<Integer> ids;

        public PeerUpdateService(ArrayList<Integer> ids) {
            this.ids = ids;
            setPeriod(new Duration(Settings.peersUpdateInterval));
        }

        protected Task<ObservableList<TorrentField.Peer>> createTask() {
            return new Task<ObservableList<TorrentField.Peer>>() {
                @Override
                protected ObservableList<TorrentField.Peer> call() throws Exception {
                    List<String> fields = Collections.singletonList("peers");
                    TorrentGetArguments args = (TorrentGetArguments) (client.send(new TorrentGet(ids).setFields(fields)).getArguments());
                    List<TorrentField.Peer> list = args.getFields().get(0).getPeers();
                    peers.setAll(list);
                    return peers;
                }
            };
        }
    }
}
