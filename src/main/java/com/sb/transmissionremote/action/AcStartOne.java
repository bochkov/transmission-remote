package com.sb.transmissionremote.action;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqTorrent;
import cordelia.jsonrpc.req.RqTorrentStart;
import cordelia.jsonrpc.req.types.Ids;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class AcStartOne extends AcError {

    private final AtomicReference<TrClient> client;
    private final Supplier<List<Object>> supplier;

    public AcStartOne(AtomicReference<TrClient> client, Supplier<List<Object>> supplier) {
        super("Start one", TransmissionRemote.ICON_REPLY);
        this.client = client;
        this.supplier = supplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RqTorrent.Params params = RqTorrent.Params.builder()
                .ids(Ids.any(supplier.get()))
                .build();
        RqTorrentStart req = new RqTorrentStart(TransmissionRemote.TAG, params);
        client.get().execute(req);
    }
}
