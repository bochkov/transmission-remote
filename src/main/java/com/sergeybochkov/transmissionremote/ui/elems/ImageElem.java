package com.sergeybochkov.transmissionremote.ui.elems;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public final class ImageElem implements Element {

    private final String filename;

    public ImageElem(String filename) {
        this.filename = filename;
    }

    @Override
    public Node graphic() {
        ImageView view = new ImageView();
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        InputStream stream = getClass().getResourceAsStream(String.format("/filetypes/%s.png", extension));
        if (stream == null)
            stream = getClass().getResourceAsStream("/filetypes/folder.png");
        if (stream != null)
            view.setImage(new Image(stream));
        return view;
    }
}
