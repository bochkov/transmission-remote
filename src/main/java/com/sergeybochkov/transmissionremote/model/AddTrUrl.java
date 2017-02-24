package com.sergeybochkov.transmissionremote.model;

import cordelia.client.TrClient;
import cordelia.rpc.TorrentAdd;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AddTrUrl implements AddTr {

    private final String url;
    private final String destDir;

    public AddTrUrl(String url, String destDir) {
        this.url = url;
        this.destDir = destDir;
    }

    @Override
    public void add(TrClient client) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("filename", url);
        map.put("download-dir", destDir);
        client.post(new TorrentAdd(map));
    }

    @Override
    public List<File> files() {
        return Collections.emptyList();
    }
}
