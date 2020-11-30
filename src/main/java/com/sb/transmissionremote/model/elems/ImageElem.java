package com.sb.transmissionremote.model.elems;

import java.net.URL;
import javax.swing.*;

import com.sb.transmissionremote.TransmissionRemote;

public final class ImageElem implements Element {

    private static final URL FOLDER = TransmissionRemote.class.getResource("/filetypes/folder.png");
    private final String filename;

    public ImageElem(String filename) {
        this.filename = filename;
    }

    @Override
    public JComponent graphic() {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        URL resource = TransmissionRemote.class.getResource(String.format("/filetypes/%s.png", extension));
        if (resource != null)
            return new JLabel(new ImageIcon(resource));

        return new JLabel(new ImageIcon(FOLDER));
    }
}
