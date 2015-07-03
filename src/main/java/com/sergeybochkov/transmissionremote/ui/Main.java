package com.sergeybochkov.transmissionremote.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sergeybochkov.transmissionremote.controller.Client;
import com.sergeybochkov.transmissionremote.helpers.Properties;
import com.sergeybochkov.transmissionremote.helpers.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends Application {

    private static final File PROPS = new File(Settings.settingsDir, Settings.settingsFile);
    private static Properties properties = new Properties();

    public static void main(String[] args){
        for (String arg : args)
            if (arg.equals("--debug"))
                Settings.debug = true;

        loadProperties();
        Application.launch(args);
    }

    public static Properties getProperties() {
        return properties;
    }

    private static void loadProperties(){
        Gson gson = new GsonBuilder().create();
        try {
            boolean created = false;
            if (!PROPS.exists())
                created = PROPS.getParentFile().mkdirs() & PROPS.createNewFile();
            if (created)
                System.out.println("Property file not found; created a new one");

            properties = gson.fromJson(new FileReader(PROPS), properties.getClass());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static void saveProperties() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter(PROPS);
            gson.toJson(properties, writer);
            writer.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(getClass().getResource(Settings.fontAwesome).toExternalForm(), 14);

        FXMLLoader loader = new FXMLLoader();
        Stage root = loader.load(getClass().getResourceAsStream("/ui/main_layout.fxml"));
        root.setMinHeight(Settings.minHeight);
        root.setMinWidth(Settings.minWidth);
        root.show();

        root.setOnCloseRequest(event -> {
            if (properties == null)
                properties = new Properties();
            properties.setStageHeight((int) root.getHeight());
            properties.setStageWidth((int) root.getWidth());
            if (Client.getInstance() != null) {
                properties.setClientHost(Client.getInstance().getServer());
                properties.setClientPort(Client.getInstance().getPort());
                properties.setClientUsername(Client.getInstance().getUsername());
                properties.setClientPassword(Client.getInstance().getPassword());
            }
            saveProperties();
        });
    }
}