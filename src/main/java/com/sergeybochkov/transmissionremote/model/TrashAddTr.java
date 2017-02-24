package com.sergeybochkov.transmissionremote.model;

import cordelia.client.TrClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class TrashAddTr implements AddTr {

    private final AddTr origin;
    private final File trashDir;

    public TrashAddTr(AddTr origin) {
        this.origin = origin;
        this.trashDir = new File(System.getProperty("user.home"), ".Trash");
    }

    @Override
    public void add(TrClient client) throws IOException {
        this.origin.add(client);
        for (File file : files())
            if (file.exists() && trashDir.exists())
                FileUtils.moveFileToDirectory(file, trashDir, false);
    }

    @Override
    public List<File> files() {
        return this.origin.files();
    }
}
