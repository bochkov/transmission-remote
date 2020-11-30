package com.sb.transmissionremote.ui;

import javax.swing.*;

public final class TMenuItem extends JMenuItem {

    public TMenuItem(Action action, KeyStroke keyStroke) {
        super(action);
        setAccelerator(keyStroke);
    }

    public TMenuItem(String text) {
        super(text);
    }

    public TMenuItem(Action action) {
        super(action);
    }
}
