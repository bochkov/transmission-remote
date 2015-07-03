package com.sergeybochkov.transmissionremote.controller;

import com.sergeybochkov.transmissionremote.helpers.Settings;
import com.sergeybochkov.transmissionremote.helpers.TorrentFieldWrap;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentField;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentGetArguments;
import com.sergeybochkov.transmissionremote.model.rpc.methods.TorrentGet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.TreeTableView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InfoFileController {

    private static Client client = Client.getInstance();
    private ObservableList<TorrentFieldWrap> files = FXCollections.observableArrayList();
    private FileUpdateService service;

    @SuppressWarnings("unchecked")
    public InfoFileController(ArrayList<Integer> ids, TreeTableView<TorrentFieldWrap> treeView) {
        service = new FileUpdateService(ids);
        service.setOnSucceeded(event -> {
            // запоминаем выделение
            List<Integer> indexes = treeView.getSelectionModel().getSelectedIndices().stream().collect(Collectors.toList());
            files.setAll(service.getValue());
            // восстанавливаем выделение
            indexes.forEach(i -> treeView.getSelectionModel().select(i));
        });

    }

    public ObservableList<TorrentFieldWrap> getObservableFiles() {
        return files;
    }

    public FileUpdateService getService() {
        return service;
    }

    private static class FileUpdateService extends ScheduledService<ObservableList<TorrentFieldWrap>> {

        private ArrayList<Integer> ids;
        private ObservableList<TorrentFieldWrap> files = FXCollections.observableArrayList();

        public FileUpdateService(ArrayList<Integer> ids) {
            this.ids = ids;
            setPeriod(new Duration(Settings.filesUpdateInterval));
        }

        @Override
        protected Task<ObservableList<TorrentFieldWrap>> createTask() {
            return new Task<ObservableList<TorrentFieldWrap>>() {
                @Override
                protected ObservableList<TorrentFieldWrap> call() throws Exception {
                    List<String> fields = Arrays.asList("files", "fileStats");
                    TorrentGetArguments args = (TorrentGetArguments) client.send(new TorrentGet(ids).setFields(fields)).getArguments();

                    TorrentField tf = args.getFields().get(0);
                    ArrayList<TorrentFieldWrap> resArray = new ArrayList<>();
                    for (int i = 0; i < tf.getFiles().size(); ++i)
                        resArray.add(new TorrentFieldWrap(i, tf.getFiles().get(i), tf.getFileStats().get(i)));
                    files.setAll(resArray);
                    Collections.sort(files);
                    return files;
                }
            };
        }
    }
}
