package com.sb.transmissionremote.action;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqSessionSet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicReference;

public final class AcSpeedLimit extends AcError {

    private final AtomicReference<TrClient> client;

    public AcSpeedLimit(AtomicReference<TrClient> client) {
        super("Speed limit", TransmissionRemote.ICON_ROCKET);
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton btn = (JToggleButton) e.getSource();
        RqSessionSet.Params params = RqSessionSet.Params.builder()
                .altSpeedEnabled(btn.isSelected())
                .build();
        RqSessionSet req = new RqSessionSet(TransmissionRemote.TAG, params);
        client.get().execute(req);
    }
}
