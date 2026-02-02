package com.sb.transmissionremote.model;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqTorrentAdd;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public final class TrSourceUrl implements TrSource {

    private final String url;
    private final String downloadDir;

    @Override
    public void add(TrClient client) {
        RqTorrentAdd.Params params = RqTorrentAdd.Params.builder()
                .filename(url)
                .downloadDir(downloadDir)
                .build();
        RqTorrentAdd req = new RqTorrentAdd(TransmissionRemote.TAG, params);
        client.execute(req);
    }

    @Override
    public List<File> files() {
        return Collections.emptyList();
    }
}
