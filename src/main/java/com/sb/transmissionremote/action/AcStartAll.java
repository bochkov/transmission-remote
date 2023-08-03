package com.sb.transmissionremote.action;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.rpc.RqTorrent;
import cordelia.rpc.types.TorrentAction;

public final class AcStartAll extends AcError {

    private final AtomicReference<TrClient> client;
    private final Supplier<List<Object>> supplier;

    public AcStartAll(AtomicReference<TrClient> client, Supplier<List<Object>> supplier) {
        super("Start all", TransmissionRemote.ICON_REPLY_ALL);
        this.client = client;
        this.supplier = supplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        client.get().execute(new RqTorrent(TorrentAction.START, supplier.get()));
    }
}
