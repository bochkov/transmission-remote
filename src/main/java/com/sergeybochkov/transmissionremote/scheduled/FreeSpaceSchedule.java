package com.sergeybochkov.transmissionremote.scheduled;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.model.Size;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
import cordelia.rpc.FreeSpace;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public final class FreeSpaceSchedule extends ScheduledService<Size> {

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
    protected Task<Size> createTask() {
        return new Task<Size>() {
            @Override
            protected Size call() throws Exception {
                return new Size( (Double)
                        client.post(new FreeSpace(downloadDir), TrResponse.class)
                                .arguments()
                                .get("size-bytes"));
            }
        };
    }
}
