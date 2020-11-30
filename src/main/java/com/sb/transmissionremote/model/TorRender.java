package com.sb.transmissionremote.model;

import java.awt.*;
import javax.swing.*;

public final class TorRender implements ListCellRenderer<Tor> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Tor> list, Tor value, int index, boolean isSelected, boolean cellHasFocus) {
        JComponent cmp = value.graphic();
        cmp.setOpaque(true);
        if (isSelected) {
            cmp.setBackground(new Color(228, 230, 239));
        } else {
            cmp.setBackground(UIManager.getColor("JPanel.background"));
        }
        return cmp;
    }
}
