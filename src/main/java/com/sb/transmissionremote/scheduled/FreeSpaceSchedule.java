package com.sb.transmissionremote.scheduled;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import cordelia.client.TrClient;
import cordelia.client.TypedResponse;
import cordelia.rpc.RqFreeSpace;
import cordelia.rpc.RsFreeSpace;
import cordelia.rpc.RsSessionGet;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FreeSpaceSchedule implements Runnable {

    private final AtomicReference<TrClient> client;
    private final AtomicReference<RsSessionGet> session;
    private final Consumer<RsFreeSpace> consumer;

    @Override
    public void run() {
        String downloadDir = session.get().getDownloadDir();
        TypedResponse<RsFreeSpace> rs = client.get().execute(new RqFreeSpace(downloadDir));
        consumer.accept(rs.getArgs());
    }
}
