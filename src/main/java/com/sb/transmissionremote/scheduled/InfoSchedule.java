package com.sb.transmissionremote.scheduled;

import java.util.Arrays;
import java.util.function.Consumer;

import com.sb.transmissionremote.model.TorResponse;
import cordelia.client.TrClient;
import cordelia.rpc.TorrentGet;

public final class InfoSchedule implements Runnable {

    private final TrClient client;
    private final Integer id;
    private final Consumer<TorResponse> consumer;

    public InfoSchedule(TrClient client, Integer id, Consumer<TorResponse> consumer) {
        this.client = client;
        this.id = id;
        this.consumer = consumer;
    }

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
