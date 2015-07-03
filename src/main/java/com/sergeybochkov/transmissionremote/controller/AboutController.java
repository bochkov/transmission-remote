package com.sergeybochkov.transmissionremote.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML
    private Label dateLabel;

    public void navigate(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(url));
        }
        catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private String createDate(){
        String copy = "Sergey Bochkov, 2013";
        int year = new GregorianCalendar().get(Calendar.YEAR);
        if (year > 2013)
            copy += "-" + year;
        return copy;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateLabel.setText(createDate());
    }
}
