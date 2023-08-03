package com.sb.transmissionremote.action;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.rpc.RqTorrentRemove;

public final class AcRemoveData extends AcError {

    private final AtomicReference<TrClient> client;
    private final Supplier<List<Object>> supplier;

    public AcRemoveData(AtomicReference<TrClient> client, Supplier<List<Object>> supplier) {
        super("Remove torrent with data", TransmissionRemote.ICON_BAN);
        this.client = client;
        this.supplier = supplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        client.get().execute(new RqTorrentRemove(supplier.get(), true));
    }
}
