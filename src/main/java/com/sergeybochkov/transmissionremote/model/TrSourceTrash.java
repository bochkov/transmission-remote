package com.sergeybochkov.transmissionremote.model;

import cordelia.client.TrClient;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class TrSourceTrash implements TrSource {

    private final TrSource origin;

    public TrSourceTrash(TrSource origin) {
        this.origin = origin;
    }

    @Override
    public void add(TrClient client) throws IOException {
        this.origin.add(client);
        for (File file : files())
            if (file.exists())
                Desktop.getDesktop().moveToTrash(file);
    }

    @Override
    public List<File> files() {
        return this.origin.files();
    }
}
