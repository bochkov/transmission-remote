package com.sergeybochkov.transmissionremote.helpers;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class Helpers {

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("mac");
    }

    public static KeyCodeCombination getSessionKey() {
        if (isMac())
            return new KeyCodeCombination(KeyCode.N, KeyCombination.META_DOWN);
        else
            return new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
    }

    public static KeyCodeCombination getAddTorrentKey() {
        if (isMac())
            return new KeyCodeCombination(KeyCode.D, KeyCombination.META_DOWN);
        else
            return new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
    }

    public static Integer speedToInt(String speed) {
        String[] parts = speed.split(" ");
        String[] sizes = new String[]{"B/s", "kB/s", "MB/s", "GB/s", "TB/s"};
        int multiply = 0;
        Double value = Double.valueOf(parts[0].replace(",", "."));
        for (String size : sizes) {
            if (parts[1].equals(size))
                break;
            ++multiply;
        }

        if (multiply == 0)
            return (int) (value * 1);
        return (int) (value * multiply * 1000);
    }

    public static String humanizeSpeed(int rate) {
        double res = (double) rate;
        int count = 0;
        while (res >= 1000) {
            res /= 1000;
            ++count;
        }
        String[] sizes = new String[]{"B/s", "kB/s", "MB/s", "GB/s", "TB/s"};
        return String.format("%.2f %s", res, sizes[count]);
    }

    public static String humanizeSize(long size) {
        double res = (double) size;
        int count = 0;
        while (res >= 1000) {
            res /= 1000;
            ++count;
        }
        String[] sizes = new String[]{"B", "kB", "MB", "GB", "TB"};
        return String.format("%.2f %s", res, sizes[count]);
    }

    public static String humanizeTime(long secs) {
        if (secs < 0) return "";
        long days = secs / 86400;
        if (days > 0)
            return days + " days";

        long hours = secs / 3600;
        long minutes = (secs % 3600) / 60;
        long seconds = secs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Label createIcon(String iconName) {
        return createIcon(iconName, 14);
    }

    public static Label createIcon(String iconName, int size) {
        Label label = new Label(iconName);
        label.setStyle(String.format("-fx-font-size: %dpx;", size));
        label.getStyleClass().add("icons");
        return label;
    }
}
