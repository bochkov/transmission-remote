package com.sb.transmissionremote.scheduled;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import cordelia.client.TrClient;
import cordelia.client.TypedResponse;
import cordelia.rpc.RqTorrentGet;
import cordelia.rpc.RsTorrentGet;
import cordelia.rpc.types.Fields;
import cordelia.rpc.types.Torrents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class TorrentSchedule implements Runnable {

    private final AtomicReference<TrClient> client;
    private final Consumer<List<Torrents>> consumer;

    @Override
    public void run() {
        TypedResponse<RsTorrentGet> rs = client.get().execute(
                new RqTorrentGet(
                        Fields.ID,
                        Fields.NAME,
                        Fields.PERCENT_DONE,
                        Fields.PEERS_SENDING_TO_US,
                        Fields.PEERS_GETTING_FROM_US,
                        Fields.SIZE_WHEN_DONE,
                        Fields.PEERS_CONNECTED,
                        Fields.STATUS,
                        Fields.RATE_DOWNLOAD,
                        Fields.RATE_UPLOAD,
                        Fields.UPLOAD_RATIO,
                        Fields.ETA,
                        Fields.ERROR,
                        Fields.ERROR_STRING
                )
        );
        consumer.accept(rs.getArgs().getTorrents());
    }
}
