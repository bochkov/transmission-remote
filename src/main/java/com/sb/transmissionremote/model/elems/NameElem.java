package com.sb.transmissionremote.model.elems;

import cordelia.jsonrpc.res.RsTorrentGet;

import javax.swing.*;
import java.awt.*;

public final class NameElem implements Element {

    private static final int MAX_LENGTH = 60;

    private final String name;

    public NameElem(RsTorrentGet.Torrents t) {
        this.name = t.getName();
    }

    @Override
    public JComponent graphic() {
        var nameLabel = new JLabel(sizedName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
        return nameLabel;
    }

    public String sizedName() {
        if (name.length() > MAX_LENGTH) {
            int dotIdx = name.lastIndexOf('.');
            if (dotIdx > 0 && name.substring(dotIdx).length() == 4) {
                return name.substring(0, MAX_LENGTH - 4) + "..." + name.substring(name.lastIndexOf('.') + 1);
            }
            return name.substring(0, MAX_LENGTH) + "...";
        }
        return name;
    }
}
