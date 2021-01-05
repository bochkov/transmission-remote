package com.sb.transmissionremote.action;

import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.Client;
import cordelia.client.TrResponse;
import cordelia.rpc.TorrentRemove;

public final class AcRemoveData extends AcError {

    private final AtomicReference<Client> client;
    private final Supplier<Object[]> supplier;

    public AcRemoveData(AtomicReference<Client> client, Supplier<Object[]> supplier) {
        super("Remove torrent with data", TransmissionRemote.ICON_BAN);
        this.client = client;
        this.supplier = supplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        client.get().post(new TorrentRemove(true, supplier.get()), TrResponse.class);
    }
}
