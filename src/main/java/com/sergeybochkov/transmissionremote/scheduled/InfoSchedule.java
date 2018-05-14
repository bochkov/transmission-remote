package com.sergeybochkov.transmissionremote.scheduled;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.model.TorResponse;
import cordelia.client.TrClient;
import cordelia.rpc.TorrentGet;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.Arrays;

public final class InfoSchedule extends ScheduledService<TorResponse> {

    private final TrClient client;
    private final Integer id;

    public InfoSchedule(TrClient client, Integer id) {
        this.client = client;
        this.id = id;
        setPeriod(new Duration(TransmissionRemote.INFO_INTERVAL));
    }

    @Override
    protected Task<TorResponse> createTask() {
        return new Task<TorResponse>() {
            @Override
            protected TorResponse call() throws Exception {
                return client.post(
                        new TorrentGet(Arrays.asList("peers", "files", "fileStats"), id),
                        TorResponse.class);
            }
        };
    }
}
