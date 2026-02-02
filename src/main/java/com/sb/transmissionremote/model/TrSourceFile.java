package com.sb.transmissionremote.model;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqTorrentAdd;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RequiredArgsConstructor
public final class TrSourceFile implements TrSource {

    private final List<File> files;
    private final String downloadDir;

    @Override
    public void add(TrClient client) throws IOException {
        for (File file : files) {
            RqTorrentAdd.Params params = RqTorrentAdd.Params.builder()
                    .metainfo(Files.readAllBytes(file.toPath()))
                    .downloadDir(downloadDir)
                    .build();
            RqTorrentAdd req = new RqTorrentAdd(TransmissionRemote.TAG, params);
            client.execute(req);
        }
    }

    @Override
    public List<File> files() {
        return files;
    }
}
