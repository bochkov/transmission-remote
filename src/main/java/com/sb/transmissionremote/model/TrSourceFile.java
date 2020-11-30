package com.sb.transmissionremote.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import cordelia.client.Client;
import cordelia.client.TrResponse;
import cordelia.rpc.TorrentAdd;

public final class TrSourceFile implements TrSource {

    private final List<File> files;
    private final Map<String, Object> args;

    public TrSourceFile(List<File> files, Map<String, Object> args) {
        this.files = new ArrayList<>(files);
        this.args = args;
    }

    @Override
    public void add(Client client) throws IOException {
        for (File file : files) {
            Map<String, Object> map = new HashMap<>();
            map.put("metainfo", Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())));
            map.put("download-dir", args.get("download-dir"));
            client.post(new TorrentAdd(map), TrResponse.class);
        }
    }

    @Override
    public List<File> files() {
        return files;
    }
}
