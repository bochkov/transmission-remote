package com.sb.transmissionremote.action;

import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.Client;
import cordelia.client.TrResponse;
import cordelia.rpc.Torrent;

public final class AcStopOne extends AcError {

    private final AtomicReference<Client> client;
    private final Supplier<Object[]> supplier;

    public AcStopOne(AtomicReference<Client> client, Supplier<Object[]> supplier) {
        super("Stop one", TransmissionRemote.ICON_SQUARE_O);
        this.client = client;
        this.supplier = supplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        client.get().post(new Torrent(Torrent.Action.STOP, supplier.get()), TrResponse.class);
    }
}
