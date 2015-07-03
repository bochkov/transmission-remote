package com.sergeybochkov.transmissionremote.controller;

import com.sergeybochkov.transmissionremote.ui.Main;
import com.sergeybochkov.transmissionremote.helpers.Helpers;
import com.sergeybochkov.transmissionremote.helpers.Properties;
import com.sergeybochkov.transmissionremote.helpers.Settings;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentField;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppController implements Initializable {

    private ServiceController serviceController;
    private static Properties properties = Main.getProperties();

    @FXML
    private MenuItem sessionItem, addItem, exitItem, aboutItem;
    @FXML
    private MenuItem startAllItem, stopAllItem, startItem, stopItem, infoItem, reannounceItem, trashItem, deleteItem;
    @FXML
    private Button startAllButton, stopAllButton, trashButton, infoButton;
    @FXML
    private ToggleButton speedLimitButton;
    @FXML
    private ListView<TorrentField> torrentList;
    @FXML
    private MenuItem startContextItem, stopContextItem, reannounceContextItem, infoContextItem, trashContextItem, deleteContextItem;
    @FXML
    private Label freeSpaceLabel, downSpeedLabel, upSpeedLabel, ratingLabel;
    @FXML
    private Stage primaryStage;
    @FXML
    private MenuBar menuBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBar.setUseSystemMenuBar(true);
        setAccelerators();
        if (properties != null) {
            primaryStage.setWidth(properties.getStageWidth());
            primaryStage.setHeight(properties.getStageHeight());

            Platform.runLater(() -> {
                if (properties.getClientHost() != null)
                    updateServiceController(properties.getClientHost(), properties.getClientPort(),
                            properties.getClientUsername(), properties.getClientPassword());
            });
        }
        Platform.runLater(() -> {
            if (serviceController == null)
                session();
        });
    }

    private void configureList(){
        torrentList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        torrentList.setOnMouseClicked((MouseEvent ev) -> {
            EventTarget target = ev.getTarget();
            if (target instanceof ListCell) {
                ListCell cell = (ListCell) target;
                if (cell.getGraphic() == null)
                    torrentList.getSelectionModel().select(-1);
            }
            ev.consume();
        });
        torrentList.setOnDragOver((DragEvent ev) -> {
            ev.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            ev.consume();
        });
        torrentList.setOnDragDropped((DragEvent ev) -> {
            Dragboard db = ev.getDragboard();
            List<File> files = db.getFiles();
            if (files.size() == 0)
                serviceController.addTorrentByUrl(db.getUrl());
            else
                files.forEach(this::addFileToDownload);

            ev.setDropCompleted(true);
            ev.consume();
        });
    }

    private void setAccelerators() {
        sessionItem.setAccelerator(Helpers.getSessionKey());
        addItem.setAccelerator(Helpers.getAddTorrentKey());
    }

    private void updateActions(){
        // --- File menu ---
        sessionItem.setOnAction(event -> session());
        addItem.setOnAction(event -> add());
        exitItem.setOnAction(event -> Platform.exit());
        // --- Edit menu ---
        startAllItem.setOnAction(event -> serviceController.startTorrents(getAllIds()));
        stopAllItem.setOnAction(event -> serviceController.stopTorrents(getAllIds()));
        startItem.setOnAction(event -> serviceController.startTorrents(getSelectionIds()));
        stopItem.setOnAction(event -> serviceController.stopTorrents(getSelectionIds()));
        infoItem.setOnAction(event -> info());
        reannounceItem.setOnAction(event -> serviceController.reannounceTorrents(getAllIds()));
        trashItem.setOnAction(event -> serviceController.deleteTorrent(getSelectionIds(), false));
        deleteItem.setOnAction(event -> serviceController.deleteTorrent(getSelectionIds(), true));
        // --- Help menu ---
        aboutItem.setOnAction(event -> about());
        // --- Toolbar buttons ---
        startAllButton.setOnAction(event -> serviceController.startTorrents(getAllIds()));
        stopAllButton.setOnAction(event -> serviceController.stopTorrents(getAllIds()));
        speedLimitButton.setOnAction(event -> serviceController.setAltSpeedEnable(speedLimitButton.isSelected()));
        speedLimitButton.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (speedLimitButton.isSelected()) {
                speedLimitButton.setGraphic(Helpers.createIcon(Settings.ICON_ANCHOR));
                speedLimitButton.setTooltip(new Tooltip("Now speed limit is ON"));
            } else {
                speedLimitButton.setGraphic(Helpers.createIcon(Settings.ICON_ROCKET));
                speedLimitButton.setTooltip(new Tooltip("Now speed limit is OFF"));
            }
        });
        trashButton.setOnAction(event -> serviceController.deleteTorrent(getSelectionIds(), false));
        infoButton.setOnAction(event -> info());
        // --- Context menu ---
        startContextItem.setOnAction(event -> serviceController.startTorrents(getSelectionIds()));
        stopContextItem.setOnAction(event -> serviceController.stopTorrents(getSelectionIds()));
        reannounceContextItem.setOnAction(event -> serviceController.reannounceTorrents(getSelectionIds()));
        infoContextItem.setOnAction(event -> info());
        trashContextItem.setOnAction(event -> serviceController.deleteTorrent(getSelectionIds(), false));
        deleteContextItem.setOnAction(event -> serviceController.deleteTorrent(getSelectionIds(), true));
    }

    private void updateBindings(){
        primaryStage.titleProperty().bind(new SimpleStringProperty(Settings.appName).concat(serviceController.getTitle()));
        // --- Toolbar ---
        speedLimitButton.selectedProperty().setValue(serviceController.getSession().getAltSpeedEnabled());
        // --- Torrent List ---
        torrentList.itemsProperty().bind(new SimpleListProperty<>(serviceController.getTorrents()));
        // --- Labels ---
        freeSpaceLabel.textProperty().bind(serviceController.getFreeSpace());
        downSpeedLabel.textProperty().bind(serviceController.getDownloadSpeed());
        upSpeedLabel.textProperty().bind(serviceController.getUploadSpeed());
        ratingLabel.textProperty().bind(serviceController.getRating());
        // --- Enabled properties ---
        disabledControls(startItem, stopItem, infoItem, reannounceItem, trashItem, deleteItem);
        disabledControls(infoButton, trashButton);
        disabledControls(startContextItem, stopContextItem, reannounceContextItem, infoContextItem,
                trashContextItem, deleteContextItem);
        // --- Request intervals ---
        primaryStage.iconifiedProperty().addListener((prop, oldValue, newValue) -> {
            if (serviceController == null)
                return;
            if (newValue)
                serviceController.changeInterval(20);
            else
                serviceController.resetInterval();
        });
        primaryStage.focusedProperty().addListener((prop, oldValue, newValue) -> {
            if (serviceController == null)
                return;
            if (!newValue && !primaryStage.isIconified() && primaryStage.isShowing())
                serviceController.changeInterval(1.5);
            else if (newValue && !primaryStage.isIconified() && primaryStage.isShowing())
                serviceController.resetInterval();
        });
    }

    private void disabledControls(Object... objects){
        ObservableBooleanValue selectionIsNone = torrentList.getSelectionModel().selectedIndexProperty().isEqualTo(-1);
        for (Object object : objects) {
            if (object instanceof MenuItem)
                ((MenuItem) object).disableProperty().bind(selectionIsNone);
            else if (object instanceof Control)
                ((Control) object).disableProperty().bind(selectionIsNone);
        }
    }

    private void updateServiceController(String server, Integer port, String user, String passwd){
        if (serviceController != null)
            serviceController.terminateServices();

        Client.getInstance(server, port, user, passwd);

        serviceController = new ServiceController(torrentList);
        updateBindings();

        configureList();
        torrentList.requestFocus();
        updateActions();
    }

    private List<Integer> getSelectionIds() {
        return torrentList.getSelectionModel().getSelectedItems().stream()
                .map(TorrentField::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> getAllIds() {
        return torrentList.getItems().stream()
                .map(TorrentField::getId)
                .collect(Collectors.toList());
    }

    private void addFileToDownload(File file) {
        addFileToDownload(file, null);
    }

    private void addFileToDownload(File file, String dir) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            String meta = new Base64().encodeToString(bytes);
            if (dir != null)
                serviceController.addTorrentByMetaInfo(meta, dir);
            else
                serviceController.addTorrentByMetaInfo(meta);

            boolean deleted = file.delete();
            if (!deleted)
                System.err.println("Cannot delete " + file);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void session(){
        FXMLLoader loader = showModal("/ui/session_layout.fxml");
        SessionController controller = loader.getController();

        if (Client.getInstance() != null) {
            controller.setServerStr(Client.getInstance().getServer());
            controller.setPortVal(Client.getInstance().getPort());
            controller.setUserStr(Client.getInstance().getUsername());
            controller.setPasswordStr(Client.getInstance().getPassword());

            boolean auth = Client.getInstance().getUsername().equals("") &&
                    Client.getInstance().getPassword().equals("");
            controller.setAuthentication(auth);
        }

        ((Stage) loader.getRoot()).setOnHidden(event -> {
            if (controller.RESULT == SessionController.OK_OPTION)
                updateServiceController(controller.getServerStr(), controller.getPortVal(),
                        controller.getUserStr(), controller.getPasswordStr());
        });
    }

    private FXMLLoader showModal(String layout) {
        FXMLLoader loader = new FXMLLoader();
        try {
            Stage stage = loader.load(getClass().getResource(layout).openStream());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE)
                    stage.close();
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return loader;
    }

    private void add(){
        FXMLLoader loader = showModal("/ui/add_torrent_layout.fxml");
        AddController addController = loader.getController();

        addController.setLastPath(properties.getLastOpenDialogPath());
        if (properties.getLastDestination() == null)
            addController.setDestination(serviceController.getDownloadDir());
        else
            addController.setDestination(properties.getLastDestination());

        ((Stage) loader.getRoot()).setOnHidden(event -> {
            if (addController.RESULT == AddController.OK_OPTION) {
                // add torrent by files
                List<File> files = addController.getFiles();
                files.stream().filter(File::exists).forEach(file -> addFileToDownload(file, addController.getDestination()));
                // add torrent by url
                String meta = addController.getUrlFieldText();
                if (!meta.equals(""))
                    serviceController.addTorrentByMetaInfo(meta, addController.getDestination());
            }

            properties.setLastDestination(addController.getDestination());
            properties.setLastOpenDialogPath(addController.getLastPath());
        });
    }

    private void info(){
        TorrentField tf = torrentList.getSelectionModel().getSelectedItem();
        if (tf == null)
            return;
        FXMLLoader loader = showModal("/ui/info_layout.fxml");
        InfoController controller = loader.getController();
        controller.setServiceController(serviceController);
        controller.setTorrent(tf);
    }

    private void about(){
        showModal("/ui/about_layout.fxml");
    }
}
