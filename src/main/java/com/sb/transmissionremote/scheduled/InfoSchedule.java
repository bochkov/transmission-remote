package com.sb.transmissionremote.scheduled;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqTorrentGet;
import cordelia.jsonrpc.res.RsTorrentGet;
import cordelia.jsonrpc.types.FKey;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public final class InfoSchedule implements Runnable {

    private final TrClient client;
    private final Long tag;
    private final Consumer<RsTorrentGet.Result> consumer;

    @Override
    public void run() {
        RqTorrentGet.Params params = RqTorrentGet.Params.builder()
                .fields(List.of(FKey.PEERS, FKey.FILES, FKey.FILE_STATS))
                .build();
        RqTorrentGet req = new RqTorrentGet(TransmissionRemote.TAG, params);
        RsTorrentGet res = client.execute(req);
        consumer.accept(res.getResult());
    }
}
