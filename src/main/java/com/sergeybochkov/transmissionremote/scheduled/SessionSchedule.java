package com.sergeybochkov.transmissionremote.scheduled;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
import cordelia.rpc.SessionGet;
import cordelia.rpc.SessionStats;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class SessionSchedule extends ScheduledService<Map<String, Object>> {

    private final TrClient client;

    public SessionSchedule(TrClient client) {
        this.client = client;
        setPeriod(new Duration(TransmissionRemote.SESSION_INTERVAL));
    }

    @Override
    protected Task<Map<String, Object>> createTask() {
        return new Task<>() {
            @Override
            protected Map<String, Object> call() throws Exception {
                Map<String, Object> map = new HashMap<>();
                map.putAll(client.post(new SessionGet(), TrResponse.class).arguments());
                map.putAll(client.post(new SessionStats(), TrResponse.class).arguments());
                return Collections.unmodifiableMap(map);
            }
        };
    }
}
