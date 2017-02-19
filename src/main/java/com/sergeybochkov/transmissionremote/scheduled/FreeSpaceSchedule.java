package com.sergeybochkov.transmissionremote.scheduled;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.model.Size;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
import cordelia.rpc.FreeSpace;
import cordelia.rpc.SessionGet;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigDecimal;

public final class FreeSpaceSchedule extends ScheduledService<Size> {

    private final TrClient client;
    private final String downloadDir;

    public FreeSpaceSchedule(TrClient client) throws IOException {
        this.client = client;
        this.downloadDir = (String) client.post(new SessionGet(), TrResponse.class)
                .arguments().get("download-dir");
        setPeriod(
                new Duration(
                        TransmissionRemote.freeSpaceUpdateInterval));
    }

    @Override
    protected Task<Size> createTask() {
        return new Task<Size>() {
            @Override
            protected Size call() throws Exception {
                return new Size( (BigDecimal)
                        client.post(new FreeSpace(downloadDir), TrResponse.class)
                                .arguments()
                                .get("size-bytes"));
            }
        };
    }
}
