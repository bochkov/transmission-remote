package com.sb.transmissionremote.model.elems;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.jsonrpc.res.RsTorrentGet;

import javax.swing.*;
import java.net.URL;

public final class ImageElem implements Element {

    private final String filename;

    public ImageElem(RsTorrentGet.Torrents t) {
        this.filename = t.getName();
    }

    @Override
    public JComponent graphic() {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        URL resource = TransmissionRemote.class.getResource(String.format("/filetypes/%s.png", extension));
        if (resource != null)
            return new JLabel(new ImageIcon(resource));
        URL folderResource = TransmissionRemote.class.getResource("/filetypes/folder.png");
        if (folderResource != null)
            return new JLabel(new ImageIcon(folderResource));

        return new JLabel();
    }
}
