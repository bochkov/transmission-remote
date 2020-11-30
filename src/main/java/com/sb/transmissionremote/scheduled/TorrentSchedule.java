package com.sb.transmissionremote.scheduled;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.sb.transmissionremote.model.Tor;
import com.sb.transmissionremote.model.TorResponse;
import cordelia.client.Client;
import cordelia.rpc.TorrentGet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TorrentSchedule implements Runnable {

    private static final List<String> FIELDS = Arrays.asList(
            "id", "name", "percentDone", "peersSendingToUs",
            "peersGettingFromUs", "sizeWhenDone", "peersConnected",
            "status", "rateDownload", "rateUpload", "uploadRatio",
            "eta", "error", "errorString");

    private final AtomicReference<Client> client;
    private final Consumer<List<Tor>> consumer;

    public TorrentSchedule(AtomicReference<Client> client, Consumer<List<Tor>> consumer) {
        this.client = client;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        TorResponse resp = client.get().post(new TorrentGet(FIELDS), TorResponse.class);
        consumer.accept(resp.torrents());
    }
}
