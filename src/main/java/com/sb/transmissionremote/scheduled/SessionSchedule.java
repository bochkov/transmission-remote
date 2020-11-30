package com.sb.transmissionremote.scheduled;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import cordelia.client.Client;
import cordelia.client.TrResponse;
import cordelia.rpc.SessionGet;
import cordelia.rpc.SessionStats;

public final class SessionSchedule implements Runnable {

    private final AtomicReference<Client> client;
    private final Consumer<Map<String, Object>> consumer;

    public SessionSchedule(AtomicReference<Client> client, Consumer<Map<String, Object>> consumer) {
        this.client = client;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        Map<String, Object> map = new HashMap<>();
        map.putAll(client.get().post(new SessionGet(), TrResponse.class).arguments());
        map.putAll(client.get().post(new SessionStats(), TrResponse.class).arguments());
        consumer.accept(map);
    }
}
