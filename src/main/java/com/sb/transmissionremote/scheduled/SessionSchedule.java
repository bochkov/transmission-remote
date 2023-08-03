package com.sb.transmissionremote.scheduled;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import cordelia.client.TrClient;
import cordelia.client.TypedResponse;
import cordelia.rpc.RqSessionGet;
import cordelia.rpc.RqSessionStats;
import cordelia.rpc.RsSessionGet;
import cordelia.rpc.RsSessionStats;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class SessionSchedule implements Runnable {

    private final AtomicReference<TrClient> client;
    private final Consumer<RsSessionGet> sessionConsumer;
    private final Consumer<RsSessionStats> sessionStatsConsumer;

    @Override
    public void run() {
        TypedResponse<RsSessionGet> rs1 = client.get().execute(new RqSessionGet());
        sessionConsumer.accept(rs1.getArgs());
        TypedResponse<RsSessionStats> rs2 = client.get().execute(new RqSessionStats());
        sessionStatsConsumer.accept(rs2.getArgs());
    }
}
