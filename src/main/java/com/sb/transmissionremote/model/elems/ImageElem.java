package com.sb.transmissionremote.model.elems;

import java.net.URL;
import javax.swing.*;

import com.sb.transmissionremote.TransmissionRemote;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ImageElem implements Element {

    private final String filename;

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
