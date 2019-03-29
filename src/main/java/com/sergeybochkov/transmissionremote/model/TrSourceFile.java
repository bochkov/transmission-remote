package com.sergeybochkov.transmissionremote.model;

import cordelia.client.TrClient;
import cordelia.rpc.TorrentAdd;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class TrSourceFile implements TrSource {

    private final List<File> files;
    private final Map<String, Object> session;

    public TrSourceFile(List<File> files, Map<String, Object> session) {
        this.files = new ArrayList<>(files);
        this.session = session;
    }

    @Override
    public void add(TrClient client) throws IOException {
        for (File file : files) {
            Map<String, Object> map = new HashMap<>();
            map.put("metainfo", Base64
                    .getEncoder()
                    .encodeToString(
                            FileUtils.readFileToByteArray(file)));
            map.put("download-dir", session.get("download-dir"));
            client.post(new TorrentAdd(map));
        }
    }

    @Override
    public List<File> files() {
        return files;
    }
}
