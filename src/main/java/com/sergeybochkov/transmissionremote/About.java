package com.sergeybochkov.transmissionremote;

import com.sergeybochkov.transmissionremote.fxutil.Target;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URI;
import java.util.Calendar;

public final class About implements Target {

    private final Stage stage;
    private final AppProperties props;

    @FXML
    private Label dateLabel;

    public About(Stage stage, AppProperties props) {
        this.stage = stage;
        this.props = props;
    }

    @Override
    public void init() {
        this.stage.setResizable(false);
        String text = "Sergey Bochkov, 2013%s";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String curYear = year > 2013 ? String.format("-%s", year) : "";
        dateLabel.setText(String.format(text, curYear));
    }

    private void click(String url) throws Exception {
        java.awt.Desktop.getDesktop().browse(new URI(url));
    }

    @FXML
    private void mailClick() throws Exception {
        click("mailto:bochkov.sa@gmail.com?subject=Transmission%20Remote");
    }

    @FXML
    private void urlClick() throws Exception {
        click("http://sergeybochkov.com");
    }
}
