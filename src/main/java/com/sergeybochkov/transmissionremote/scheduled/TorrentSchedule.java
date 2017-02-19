package com.sergeybochkov.transmissionremote.scheduled;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.model.TorrentResponse;
import com.sergeybochkov.transmissionremote.model.Tr;
import cordelia.client.TrClient;
import cordelia.rpc.TorrentGet;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public final class TorrentSchedule extends ScheduledService<List<Tr>> {

    private static final List<String> FIELDS = Arrays.asList(
            "id", "name", "percentDone", "peersSendingToUs",
            "peersGettingFromUs", "sizeWhenDone", "peersConnected",
            "status", "rateDownload", "rateUpload", "uploadRatio",
            "eta", "error", "errorString");

    private final TrClient client;

    public TorrentSchedule(TrClient client) {
        this.client = client;
        setPeriod(
                new Duration(
                        TransmissionRemote.TORRENT_INTERVAL));
    }

    @Override
    protected Task<List<Tr>> createTask() {
        return new Task<List<Tr>>() {
            @Override
            protected List<Tr> call() throws Exception {
                return client.post(new TorrentGet(FIELDS), TorrentResponse.class).torrents();
            }
        };
    }
}
