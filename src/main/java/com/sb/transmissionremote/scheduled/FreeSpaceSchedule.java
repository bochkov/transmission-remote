package com.sb.transmissionremote.scheduled;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.sb.transmissionremote.model.HumanSize;
import cordelia.client.Client;
import cordelia.client.TrResponse;
import cordelia.rpc.FreeSpace;

public final class FreeSpaceSchedule implements Runnable {

    private final AtomicReference<Client> client;
    private final Map<String, Object> args;
    private final Consumer<HumanSize> consumer;

    public FreeSpaceSchedule(AtomicReference<Client> client, Map<String, Object> args, Consumer<HumanSize> consumer) {
        this.client = client;
        this.args = args;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        FreeSpace fs = new FreeSpace((String) args.get("download-dir"));
        Double size = (Double) client.get().post(fs, TrResponse.class)
                .arguments()
                .get("size-bytes");
        consumer.accept(new HumanSize(size));
    }
}
