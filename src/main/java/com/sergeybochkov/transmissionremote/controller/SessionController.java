package com.sergeybochkov.transmissionremote.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SessionController implements Initializable {

    public static final int CANCEL_OPTION = 0;
    public int RESULT = CANCEL_OPTION;
    public static final int OK_OPTION = 1;
    private String userStr = "", passwordStr = "", serverStr = "";
    private Integer portVal = 0;
    private boolean authentication;

    @FXML
    private Stage stage;
    @FXML
    private TextField serverField, portField, usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox authCheckbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::configureComponents);
    }

    private void configureComponents() {
        serverField.textProperty().setValue(serverStr);
        portField.textProperty().setValue(portVal.toString());
        usernameField.textProperty().setValue(userStr);
        passwordField.textProperty().setValue(passwordStr);
        authCheckbox.selectedProperty().setValue(authentication);

        usernameField.disableProperty().bind(authCheckbox.selectedProperty());
        passwordField.disableProperty().bind(authCheckbox.selectedProperty());
    }

    public void onCancel() {
        RESULT = CANCEL_OPTION;
        stage.close();
    }

    public void onOk() {
        serverStr = serverField.getText();
        portVal = Integer.parseInt(portField.getText());

        if (authCheckbox.isSelected()) {
            userStr = "";
            passwordStr = "";
        }
        else {
            passwordStr = passwordField.getText();
            userStr = usernameField.getText();
        }

        RESULT = OK_OPTION;
        stage.close();
    }

    public String getUserStr() {
        return userStr;
    }

    public void setUserStr(String userStr) {
        this.userStr = userStr;
    }

    public String getPasswordStr() {
        return passwordStr;
    }

    public void setPasswordStr(String passwordStr) {
        this.passwordStr = passwordStr;
    }

    public String getServerStr() {
        return serverStr;
    }

    public void setServerStr(String serverStr) {
        this.serverStr = serverStr;
    }

    public Integer getPortVal() {
        return portVal;
    }

    public void setPortVal(Integer portVal) {
        this.portVal = portVal;
    }

    public void setAuthentication(boolean auth) {
        this.authentication = auth;
    }
}
