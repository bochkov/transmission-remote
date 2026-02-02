package com.sb.transmissionremote.scheduled;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqTorrentGet;
import cordelia.jsonrpc.res.RsTorrentGet;
import cordelia.jsonrpc.types.FKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public final class TorrentSchedule implements Runnable {

    private final AtomicReference<TrClient> client;
    private final Consumer<RsTorrentGet.Result> consumer;

    @Override
    public void run() {
        RqTorrentGet.Params params = RqTorrentGet.Params.builder()
                .fields(List.of(
                        FKey.ID,
                        FKey.HASH_STRING,
                        FKey.NAME,
                        FKey.PERCENT_DONE,
                        FKey.PEERS_SENDING_TO_US,
                        FKey.PEERS_GETTING_FROM_US,
                        FKey.SIZE_WHEN_DONE,
                        FKey.PEERS_CONNECTED,
                        FKey.STATUS,
                        FKey.RATE_DOWNLOAD,
                        FKey.RATE_UPLOAD,
                        FKey.UPLOAD_RATIO,
                        FKey.ETA,
                        FKey.ERROR,
                        FKey.ERROR_STRING))
                .build();
        RqTorrentGet req = new RqTorrentGet(TransmissionRemote.TAG, params);
        RsTorrentGet res = client.get().execute(req);
        LOG.debug("{}", res);
        consumer.accept(res.getResult());
    }
}
