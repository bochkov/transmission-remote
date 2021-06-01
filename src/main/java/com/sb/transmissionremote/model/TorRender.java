package com.sb.transmissionremote.model;

import java.awt.*;
import javax.swing.*;

public final class TorRender implements ListCellRenderer<Tor> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Tor> list, Tor value, int index, boolean isSelected, boolean cellHasFocus) {
        JComponent cmp = value.graphic();
        cmp.setOpaque(true);
        cmp.setBackground(isSelected ? new Color(228, 230, 239) : UIManager.getColor("JPanel.background"));
        return cmp;
    }
}
