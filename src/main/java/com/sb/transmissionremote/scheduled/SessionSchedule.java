package com.sb.transmissionremote.scheduled;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqSessionGet;
import cordelia.jsonrpc.req.RqSessionStats;
import cordelia.jsonrpc.res.RsSessionGet;
import cordelia.jsonrpc.res.RsSessionStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public final class SessionSchedule implements Runnable {

    private final AtomicReference<TrClient> client;
    private final Consumer<RsSessionGet> sessionConsumer;
    private final Consumer<RsSessionStats> sessionStatsConsumer;

    @Override
    public void run() {
        getSession();
        getSessionStats();
    }

    private void getSession() {
        RqSessionGet.Params params = RqSessionGet.Params.builder().build();
        RqSessionGet req = new RqSessionGet(TransmissionRemote.TAG, params);
        RsSessionGet res = client.get().execute(req);
        LOG.debug("{}", res);
        sessionConsumer.accept(res);
    }

    private void getSessionStats() {
        RqSessionStats req = new RqSessionStats(TransmissionRemote.TAG);
        RsSessionStats res = client.get().execute(req);
        LOG.debug("{}", res);
        sessionStatsConsumer.accept(res);
    }
}
