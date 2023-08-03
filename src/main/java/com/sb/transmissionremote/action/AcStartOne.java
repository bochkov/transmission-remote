package com.sb.transmissionremote.action;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.rpc.RqTorrent;
import cordelia.rpc.types.TorrentAction;

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
        client.get().execute(new RqTorrent(TorrentAction.START, supplier.get()));
    }
}
