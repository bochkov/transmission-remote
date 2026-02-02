package com.sb.transmissionremote.scheduled;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqFreeSpace;
import cordelia.jsonrpc.res.RsFreeSpace;
import cordelia.jsonrpc.res.RsSessionGet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public final class FreeSpaceSchedule implements Runnable {

    private final AtomicReference<TrClient> client;
    private final AtomicReference<RsSessionGet.Result> session;
    private final Consumer<RsFreeSpace.Result> consumer;

    @Override
    public void run() {
        String downloadDir = session.get().getDownloadDir();
        RqFreeSpace.Params params = RqFreeSpace.Params.builder()
                .path(downloadDir)
                .build();
        RqFreeSpace req = new RqFreeSpace(TransmissionRemote.TAG, params);
        RsFreeSpace res = client.get().execute(req);
        LOG.debug("{}", res);
        consumer.accept(res.getResult());
    }
}
