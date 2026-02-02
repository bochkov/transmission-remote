package com.sb.transmissionremote.action;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqTorrent;
import cordelia.jsonrpc.req.RqTorrentReannounce;
import cordelia.jsonrpc.req.types.Ids;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class AcReannounce extends AcError {

    private final AtomicReference<TrClient> client;
    private final Supplier<List<Object>> supplier;

    public AcReannounce(AtomicReference<TrClient> client, Supplier<List<Object>> supplier) {
        super("Reannounce all torrents", TransmissionRemote.ICON_REFRESH);
        this.client = client;
        this.supplier = supplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RqTorrent.Params params = RqTorrent.Params.builder()
                .ids(Ids.any(supplier.get()))
                .build();
        RqTorrentReannounce req = new RqTorrentReannounce(TransmissionRemote.TAG, params);
        client.get().execute(req);
    }
}
