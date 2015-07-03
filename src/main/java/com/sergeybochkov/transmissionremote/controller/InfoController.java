package com.sergeybochkov.transmissionremote.controller;

import com.sergeybochkov.transmissionremote.helpers.TorrentFieldWrap;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class InfoController implements Initializable {

    @FXML
    private Label nameLabel;
    @FXML
    private TableView<TorrentField.Peer> peerView;
    @FXML
    private TreeTableView<TorrentFieldWrap> fileView;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private CheckMenuItem wantedMenuItem;
    @FXML
    private RadioMenuItem lowPriorityMenuItem, normalPriorityMenuItem, highPriorityMenuItem;
    @FXML
    private Stage stage;

    private TorrentField torrentField;
    private ArrayList<ScheduledService> pool = new ArrayList<>(2);
    private InfoPeerController infoPeerController;
    private InfoFileController infoFileController;
    private ServiceController serviceController = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stage.setOnCloseRequest(event -> {
            pool.forEach(ScheduledService::cancel);
            pool.clear();
        });
    }

    public void setServiceController(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    private void updateBindings() {
        nameLabel.textProperty().bind(new SimpleStringProperty(torrentField.getName()));

        peerView.setItems(infoPeerController.getObservablePeers());
        peerView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ObservableList<TorrentFieldWrap> fileStats = infoFileController.getObservableFiles();
        fileStats.addListener((ListChangeListener<TorrentFieldWrap>) change -> {
            for (TorrentFieldWrap fileStat : change.getList())
                setItem(fileStat, fileView.getRoot());
        });
        fileView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        /*
        SimpleBooleanProperty enabledContext = new SimpleBooleanProperty();
        fileView.contextMenuProperty().bind(Bindings.when(enabledContext).then(contextMenu).otherwise((ContextMenu) null));
        fileView.onMouseClickedProperty().setValue((MouseEvent ev) -> {
            Integer selected = fileView.getSelectionModel().selectedIndexProperty().getValue();
            Object object = fileView.getColumns().get(1).getCellObservableValue(selected);
            System.out.println(object != null);
            enabledContext.setValue(object != null);
            ev.consume();
        });
        */
        contextMenu.setOnShowing(ev -> {
            Integer selected = fileView.getSelectionModel().selectedIndexProperty().getValue();

            Boolean isWanted = (Boolean) fileView.getColumns().get(3).getCellObservableValue(selected).getValue();
            wantedMenuItem.selectedProperty().set(isWanted);

            TorrentFieldWrap tfw = (TorrentFieldWrap) fileView.getColumns().get(4).getCellObservableValue(selected).getValue();
            switch (tfw.getPriority()) {
                case -1:
                    lowPriorityMenuItem.setSelected(true);
                    break;
                case 0:
                    normalPriorityMenuItem.setSelected(true);
                    break;
                case 1:
                    highPriorityMenuItem.setSelected(true);
                    break;
            }
        });

        if (serviceController != null) {
            ArrayList<Integer> ids = new ArrayList<>(1);
            ids.add(torrentField.getId());
            wantedMenuItem.setOnAction(ev -> serviceController.setWanted(wantedMenuItem.isSelected(), ids, getSelected()));
            lowPriorityMenuItem.setOnAction(ev -> serviceController.setPriority(-1, ids, getSelected()));
            normalPriorityMenuItem.setOnAction(ev -> serviceController.setPriority(0, ids, getSelected()));
            highPriorityMenuItem.setOnAction(ev -> serviceController.setPriority(1, ids, getSelected()));
        }
    }

    private List<Integer> getSelected() {
        return fileView.getSelectionModel().getSelectedItems().stream()
                .map(e -> e.getValue().getInnerId())
                .collect(Collectors.toList());
    }

    private void setItem(TorrentFieldWrap stat, TreeItem<TorrentFieldWrap> parent) {
        String[] parts = stat.getName().split("/");
        String filename = parts[parts.length - 1];

        TreeItem<TorrentFieldWrap> parentRef = parent;
        for (String part : parts) {

            boolean finded = false;
            for (TreeItem<TorrentFieldWrap> item : parentRef.getChildren()) {
                if (item.getValue().getName().equals(part)) {
                    parentRef = item;
                    finded = true;
                    break;
                }
            }

            if (!finded) {
                TreeItem<TorrentFieldWrap> node;
                if (part.equals(filename)) {
                    node = new TreeItem<>(stat);
                    node.getValue().setName(part);
                } else
                    node = new TreeItem<>(new TorrentFieldWrap(part));

                node.setExpanded(true);
                parentRef.getChildren().add(node);
                parentRef = node;
            } else {
                if (parentRef.isLeaf()) {
                    parentRef.setValue(stat);
                    parentRef.getValue().setName(part);
                }
            }
        }
    }

    public void setTorrent(TorrentField tf) {
        torrentField = tf;
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(tf.getId());

        infoPeerController = new InfoPeerController(ids, peerView);
        infoFileController = new InfoFileController(ids, fileView);

        updateBindings();

        pool.add(infoPeerController.getService());
        pool.add(infoFileController.getService());
        pool.forEach(ScheduledService::start);
    }
}
