package com.sb.transmissionremote.action;

import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.TrClient;
import cordelia.rpc.RqSessionSet;

public final class AcSpeedLimit extends AcError {

    private final AtomicReference<TrClient> client;

    public AcSpeedLimit(AtomicReference<TrClient> client) {
        super("Speed limit", TransmissionRemote.ICON_ROCKET);
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton btn = (JToggleButton) e.getSource();
        RqSessionSet rq = RqSessionSet.builder()
                .altSpeedEnabled(btn.isSelected())
                .build();
        client.get().execute(rq);
    }
}
