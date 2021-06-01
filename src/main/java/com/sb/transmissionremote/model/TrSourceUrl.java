package com.sb.transmissionremote.model;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cordelia.client.Client;
import cordelia.client.TrResponse;
import cordelia.rpc.TorrentAdd;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TrSourceUrl implements TrSource {

    private final String url;
    private final Map<String, Object> args;

    @Override
    public void add(Client client) {
        Map<String, Object> map = new HashMap<>();
        map.put("filename", url);
        map.put("download-dir", args.get("download-dir"));
        client.post(new TorrentAdd(map), TrResponse.class);
    }

    @Override
    public List<File> files() {
        return Collections.emptyList();
    }
}
