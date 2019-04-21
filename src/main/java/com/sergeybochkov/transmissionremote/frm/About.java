package com.sergeybochkov.transmissionremote.frm;

import com.sergeybochkov.transmissionremote.fxutil.Target;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;

public final class About implements Target {

    private final Stage stage;

    @FXML
    private Label dateLabel;

    public About(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init() {
        this.stage.setResizable(false);
        String text = "Sergey Bochkov, 2013%s";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String curYear = year > 2013 ? String.format("-%s", year) : "";
        dateLabel.setText(String.format(text, curYear));
    }

    @FXML
    private void mailClick() throws IOException {
        Desktop.getDesktop().mail(URI.create("mailto:bochkov.sa@gmail.com?subject=Transmission%20Remote"));
    }

    @FXML
    private void urlClick() throws IOException {
        Desktop.getDesktop().browse(URI.create("http://sergeybochkov.com"));
    }
}
