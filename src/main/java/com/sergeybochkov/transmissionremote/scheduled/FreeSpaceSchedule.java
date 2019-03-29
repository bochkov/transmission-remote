package com.sergeybochkov.transmissionremote.scheduled;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.model.HumanSize;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
import cordelia.rpc.FreeSpace;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.Map;

public final class FreeSpaceSchedule extends ScheduledService<HumanSize> {

    private final TrClient client;
    private final Map<String, Object> session;

    public FreeSpaceSchedule(TrClient client, Map<String, Object> session) {
        this.client = client;
        this.session = session;
        setPeriod(new Duration(TransmissionRemote.FREE_SPACE_INTERVAL));
    }

    @Override
    protected Task<HumanSize> createTask() {
        return new Task<HumanSize>() {
            @Override
            protected HumanSize call() throws Exception {
                FreeSpace fs = new FreeSpace((String) session.get("download-dir"));
                Double size = (Double) client.post(fs, TrResponse.class)
                        .arguments()
                        .get("size-bytes");
                return new HumanSize(size);
            }
        };
    }
}
