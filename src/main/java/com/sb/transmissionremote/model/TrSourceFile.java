package com.sb.transmissionremote.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import cordelia.client.TrClient;
import cordelia.rpc.RqTorrentAdd;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TrSourceFile implements TrSource {

    private final List<File> files;
    private final String downloadDir;

    @Override
    public void add(TrClient client) throws IOException {
        for (File file : files) {
            RqTorrentAdd rq = RqTorrentAdd.builder()
                    .metainfo(Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())))
                    .downloadDir(downloadDir)
                    .build();
            client.execute(rq);
        }
    }

    @Override
    public List<File> files() {
        return files;
    }
}
