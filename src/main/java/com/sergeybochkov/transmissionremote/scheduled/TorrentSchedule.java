package com.sergeybochkov.transmissionremote.scheduled;

import com.sergeybochkov.transmissionremote.TransmissionRemote;
import com.sergeybochkov.transmissionremote.model.Tor;
import com.sergeybochkov.transmissionremote.model.TorResponse;
import cordelia.client.TrClient;
import cordelia.rpc.TorrentGet;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public final class TorrentSchedule extends ScheduledService<List<Tor>> {

    private static final List<String> FIELDS = Arrays.asList(
            "id", "name", "percentDone", "peersSendingToUs",
            "peersGettingFromUs", "sizeWhenDone", "peersConnected",
            "status", "rateDownload", "rateUpload", "uploadRatio",
            "eta", "error", "errorString");

    private final TrClient client;

    public TorrentSchedule(TrClient client) {
        this.client = client;
        setPeriod(new Duration(TransmissionRemote.TORRENT_INTERVAL));
    }

    @Override
    protected Task<List<Tor>> createTask() {
        return new Task<>() {
            @Override
            protected List<Tor> call() throws Exception {
                return client.post(new TorrentGet(FIELDS), TorResponse.class).torrents();
            }
        };
    }
}
