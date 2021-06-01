package com.sb.transmissionremote.scheduled;

import java.util.Arrays;
import java.util.function.Consumer;

import com.sb.transmissionremote.model.TorResponse;
import cordelia.client.TrClient;
import cordelia.rpc.TorrentGet;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class InfoSchedule implements Runnable {

    private final TrClient client;
    private final Integer id;
    private final Consumer<TorResponse> consumer;

    @Override
    public void run() {
        consumer.accept(
                client.post(
                        new TorrentGet(Arrays.asList("peers", "files", "fileStats"), id),
                        TorResponse.class
                )
        );
    }
}
