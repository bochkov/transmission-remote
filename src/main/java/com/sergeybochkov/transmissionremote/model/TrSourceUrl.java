package com.sergeybochkov.transmissionremote.model;

import cordelia.client.TrClient;
import cordelia.rpc.TorrentAdd;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TrSourceUrl implements TrSource {

    private final String url;
    private final Map<String, Object> session;

    public TrSourceUrl(String url, Map<String, Object> session) {
        this.url = url;
        this.session = session;
    }

    @Override
    public void add(TrClient client) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("filename", url);
        map.put("download-dir", session.get("download-dir"));
        client.post(new TorrentAdd(map));
    }

    @Override
    public List<File> files() {
        return Collections.emptyList();
    }
}
