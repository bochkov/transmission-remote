package com.sb.transmissionremote.model.elems;

import java.awt.*;
import javax.swing.*;

public final class NameElem implements Element {

    public final String name;

    public NameElem(String name) {
        this.name = name;
    }

    @Override
    public JComponent graphic() {
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        return nameLabel;
    }
}
