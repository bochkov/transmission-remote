package com.sergeybochkov.transmissionremote.scheduled;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.model.Peer;
import com.sergeybochkov.transmissionremote.model.TorResponse;
import cordelia.client.TrClient;
import cordelia.rpc.TorrentGet;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.Collections;
import java.util.List;

public final class PeerInfoSchedule extends ScheduledService<List<Peer>> {

    private final TrClient client;
    private final Integer id;

    public PeerInfoSchedule(TrClient client, Integer id) {
        this.client = client;
        this.id = id;
        setPeriod(new Duration(TransmissionRemote.PEERS_INFO_INTERVAL));
    }

    @Override
    protected Task<List<Peer>> createTask() {
        return new Task<List<Peer>>() {
            @Override
            protected List<Peer> call() throws Exception {
                return client.post(
                        new TorrentGet(Collections.singletonList("peers"), id),
                        TorResponse.class
                ).peers();
            }
        };
    }
}
