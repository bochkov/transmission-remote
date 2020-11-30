package com.sb.transmissionremote.ui;

import javax.swing.*;

public final class TMenu extends JMenu {

    public TMenu(String title, TMenuItem... items) {
        setText(title);
        for (TMenuItem item : items)
            if (item.getText() != null && item.getText().equals("---"))
                add(new JSeparator());
            else
                add(item);
    }
}
