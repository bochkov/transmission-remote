package com.sergeybochkov.transmissionremote;

import com.sergeybochkov.transmissionremote.fxutil.ResultCallback;
import com.sergeybochkov.transmissionremote.fxutil.Target;
import com.sergeybochkov.transmissionremote.model.Size;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
import cordelia.rpc.FreeSpace;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Add implements Target, ResultCallback {

    private final Stage stage;
    private final AppProperties props;

    @FXML
    private Button openButton;
    @FXML
    private Label filesLabel, destinationLabel;
    @FXML
    private TextField urlField, destinationField;

    private final List<File> files = new ArrayList<>();

    private TrClient client;
    private Callback callback;

    public Add(Stage stage, AppProperties props) {
        this.stage = stage;
        this.props = props;
    }

    @Override
    public Target callback(Callback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void init() {
        stage.setWidth(TransmissionRemote.MIN_WIDTH - 50);
        openButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open a torrent file");
            File dest = !new File(props.lastOpenPath()).exists() ?
                    new File(System.getProperty("user.home")) :
                    new File(props.lastOpenPath());
            chooser.setInitialDirectory(dest);
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Torrent Files", "*.torrent"));
            List<File> selected = chooser.showOpenMultipleDialog(null);
            if (selected != null && !selected.isEmpty()) {
                files.addAll(selected);
                filesLabel.setText(files.size() > 0 ?
                        files.get(0).getName() :
                        String.format("Selected %d files", files.size()));
                props.setLastOpenPath(files.get(0).getParent());
            }
        });
        destinationField.setText(props.lastDestination());
        destinationField.setOnKeyReleased(event -> destinationLabel.setText(printFS()));
    }

    public Add withClient(TrClient client) {
        this.client = client;
        destinationLabel.setText(printFS());
        return this;
    }

    @FXML
    private void onOk() throws IOException {
        props.setLastDestination(destinationField.getText());
        stage.close();
        callback.call(files, urlField.getText(), destinationField.getText());
    }

    @FXML
    private void onCancel() {
        stage.close();
    }

    private String printFS() {
        try {
            Double bytes = (Double) client
                    .post(new FreeSpace(destinationField.getText()), TrResponse.class)
                    .get("size-bytes");
            return bytes < 0 ?
                    "No such directory" :
                    String.format("Destination folder (%s free)", new Size(bytes));
        } catch (IOException ex) {
            return ex.getMessage();
        }
    }
}
