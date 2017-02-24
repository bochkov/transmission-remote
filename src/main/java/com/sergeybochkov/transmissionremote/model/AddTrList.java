package com.sergeybochkov.transmissionremote.model;

import cordelia.client.TrClient;
import cordelia.rpc.TorrentAdd;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class AddTrList implements AddTr {

    private final List<File> files;
    private final String destDir;

    public AddTrList(List<File> files, String destDir) {
        this.files = new ArrayList<>(files);
        this.destDir = destDir;
    }

    @Override
    public void add(TrClient client) throws IOException {
        for (File file : files) {
            Map<String, Object> map = new HashMap<>();
            map.put("metainfo", Base64
                    .getEncoder()
                    .encodeToString(
                            FileUtils.readFileToByteArray(file)));
            map.put("download-dir", destDir);
            client.post(new TorrentAdd(map));
        }
    }

    @Override
    public List<File> files() {
        return files;
    }
}
