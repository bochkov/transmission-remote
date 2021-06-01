package com.sb.transmissionremote.model.elems;

import java.awt.*;
import javax.swing.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class NameElem implements Element {

    private final String name;

    @Override
    public JComponent graphic() {
        var nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        return nameLabel;
    }
}
