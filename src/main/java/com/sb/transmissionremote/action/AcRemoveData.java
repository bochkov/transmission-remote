package com.sb.transmissionremote.action;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqTorrentRemove;
import cordelia.jsonrpc.req.types.Ids;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

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
        RqTorrentRemove.Params params = RqTorrentRemove.Params.builder()
                .ids(Ids.any(supplier.get()))
                .deleteLocalData(Boolean.TRUE)
                .build();
        RqTorrentRemove req = new RqTorrentRemove(TransmissionRemote.TAG, params);
        client.get().execute(req);
    }
}
