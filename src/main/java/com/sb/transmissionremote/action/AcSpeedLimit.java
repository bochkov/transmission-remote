package com.sb.transmissionremote.action;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

import com.sb.transmissionremote.TransmissionRemote;
import cordelia.client.Client;
import cordelia.client.TrResponse;
import cordelia.rpc.SessionSet;

public final class AcSpeedLimit extends AcError {

    private final AtomicReference<Client> client;

    public AcSpeedLimit(AtomicReference<Client> client) {
        super("Speed limit", TransmissionRemote.ICON_ROCKET);
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton btn = (JToggleButton) e.getSource();
        Map<String, Object> map = Map.of("alt-speed-enabled", btn.isSelected());
        client.get().post(new SessionSet(map), TrResponse.class);
    }
}
