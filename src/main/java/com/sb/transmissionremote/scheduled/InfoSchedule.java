package com.sb.transmissionremote.scheduled;

import java.util.function.Consumer;

import cordelia.client.TrClient;
import cordelia.client.TypedResponse;
import cordelia.rpc.RqTorrentGet;
import cordelia.rpc.RsTorrentGet;
import cordelia.rpc.types.Fields;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class InfoSchedule implements Runnable {

    private final TrClient client;
    private final Long tag;
    private final Consumer<RsTorrentGet> consumer;

    @Override
    public void run() {
        TypedResponse<RsTorrentGet> rs = client.execute(
                new RqTorrentGet(Fields.PEERS, Fields.FILES, Fields.FILE_STATS),
                tag
        );
        consumer.accept(rs.getArgs());
    }
}
