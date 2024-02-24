package com.sb.transmissionremote.model;

import java.awt.*;
import javax.swing.*;

import com.sb.transmissionremote.model.elems.*;
import cordelia.rpc.types.Torrents;
import net.miginfocom.swing.MigLayout;

public final class TorrentsRender implements ListCellRenderer<Torrents> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Torrents> list, Torrents value, int index, boolean isSelected, boolean cellHasFocus) {
        JComponent cmp = graphic(value);
        cmp.setOpaque(true);
        cmp.setBackground(isSelected ? new Color(228, 230, 239) : UIManager.getColor("JPanel.background"));
        return cmp;
    }

    public static JComponent graphic(Torrents t) {
        var panel = new JPanel(new MigLayout("fillx, wrap 1, insets 3 10 3 10, gap 2", "fill, grow"));
        panel.add(new ImageElem(t).graphic(), "dock west, gap 10");
        panel.add(new NameElem(t).graphic());
        panel.add(new PeersSpeedElem(t).graphic());
        panel.add(new ProgressElem(t).graphic());
        panel.add(new FileElem(t).graphic());
        return panel;
    }
}
