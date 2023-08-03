package com.sb.transmissionremote.model;

import java.io.File;
import java.util.Collections;
import java.util.List;

import cordelia.client.TrClient;
import cordelia.rpc.RqTorrentAdd;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TrSourceUrl implements TrSource {

    private final String url;
    private final String downloadDir;

    @Override
    public void add(TrClient client) {
        RqTorrentAdd rq = RqTorrentAdd.builder()
                .filename(url)
                .downloadDir(downloadDir)
                .build();
        client.execute(rq);
    }

    @Override
    public List<File> files() {
        return Collections.emptyList();
    }
}
