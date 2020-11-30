package com.sb.transmissionremote.action;

import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.Client;
import cordelia.client.TrResponse;
import cordelia.rpc.Torrent;

public final class AcStartOne extends AcError {

    private final AtomicReference<Client> client;
    private final Supplier<Object[]> supplier;

    public AcStartOne(AtomicReference<Client> client, Supplier<Object[]> supplier) {
        super("Start one", TransmissionRemote.ICON_REPLY);
        this.client = client;
        this.supplier = supplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        client.get().post(new Torrent(Torrent.Action.START, supplier.get()), TrResponse.class);
    }
}
