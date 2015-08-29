package com.sergeybochkov.transmissionremote.controller;

import com.sergeybochkov.transmissionremote.helpers.Helpers;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.FreeSpaceArguments;
import com.sergeybochkov.transmissionremote.model.rpc.methods.FreeSpace;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    private Stage stage;
    @FXML
    private Button openButton;
    @FXML
    private TextField urlField, destinationField;
    @FXML
    private Label filesLabel, destinationLabel;

    public static final int CANCEL_OPTION = 0;
    public static final int OK_OPTION = 1;

    public int RESULT = CANCEL_OPTION;

    private List<File> files;
    private String lastPath = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::configureComponents);
    }

    private void configureComponents(){

        destinationLabel.setText(getFreeSpace());
        destinationField.setOnKeyReleased(ev -> destinationLabel.setText(getFreeSpace()));

        openButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open a torrent file");
            File dest = lastPath == null || !new File(lastPath).exists() ? new File(System.getProperty("user.home")) : new File(lastPath);
            chooser.setInitialDirectory(dest);
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Torrent Files", "*.torrent"));
            files = chooser.showOpenMultipleDialog(null);
            if (files == null || files.isEmpty())
                return;
            if (files.size() == 1)
                filesLabel.setText(files.get(0).getName());
            else
                filesLabel.setText("Selected " + files.size() + " files");

            lastPath = files.get(0).getParent();
        });
    }

    public void onOk() {
        RESULT = OK_OPTION;
        stage.close();
    }

    public void onCancel() {
        RESULT = CANCEL_OPTION;
        stage.close();
    }

    private String getFreeSpace() {
        String ret;
        Client cl = Client.getInstance();
        FreeSpaceArguments args = (FreeSpaceArguments) cl.send(new FreeSpace(destinationField.getText())).getArguments();
        if (args.getSizeBytes() < 0)
            ret = "No such directory";
        else
            ret = "Destination folder (" + Helpers.humanizeSize(args.getSizeBytes()) + " free)";
        return ret;
    }

    public List<File> getFiles() { return files; }

    public String getDestination() {
        return destinationField.getText();
    }

    public void setDestination(String destination) {
        destinationField.setText(destination);
    }

    public String getUrlFieldText() {
        return urlField.getText();
    }

    public String getLastPath() {
        return lastPath;
    }

    public void setLastPath(String lastPath) {
        this.lastPath = lastPath;
    }
}
