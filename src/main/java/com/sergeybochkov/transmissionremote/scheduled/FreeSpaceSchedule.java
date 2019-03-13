package com.sergeybochkov.transmissionremote.scheduled;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.model.HumanSize;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
import cordelia.rpc.FreeSpace;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public final class FreeSpaceSchedule extends ScheduledService<HumanSize> {

    private final TrClient client;
    private final String downloadDir;

    public FreeSpaceSchedule(TrClient client, String downloadDir) {
        this.client = client;
        this.downloadDir = downloadDir;
        setPeriod(
                new Duration(
                        TransmissionRemote.FREE_SPACE_INTERVAL));
    }

    @Override
    protected Task<HumanSize> createTask() {
        return new Task<HumanSize>() {
            @Override
            protected HumanSize call() throws Exception {
                return new HumanSize( (Double)
                        client.post(new FreeSpace(downloadDir), TrResponse.class)
                                .arguments()
                                .get("size-bytes"));
            }
        };
    }
}
