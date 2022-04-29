package com.sb.transmissionremote.model.elems;

import java.awt.*;
import javax.swing.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class NameElem implements Element {

    private static final int MAX_LENGTH = 60;

    private final String name;

    @Override
    public JComponent graphic() {
        var nameLabel = new JLabel(sizedName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
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
