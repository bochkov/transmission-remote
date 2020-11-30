package com.sb.transmissionremote.util;

import java.awt.*;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public final class FlatSvgFlip extends FlatSVGIcon {

    public FlatSvgFlip(String name, int width, int height) {
        super(name, width, height);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(getIconWidth(), 0);
        g2.scale(-1, 1);
        super.paintIcon(c, g, x, y);
    }
}
