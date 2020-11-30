package com.sb.transmissionremote.ui;

import javax.swing.*;

public final class TMenuBar extends JMenuBar {

    public TMenuBar(TMenu... menus) {
        for (TMenu menu : menus)
            add(menu);
    }
}
