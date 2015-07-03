package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.SessionArguments;

public class SessionSet extends Request {

    private final String method = "session-set";
    private SessionArguments arguments = new SessionArguments();

    public SessionSet setAltSpeedEnabled(boolean enabled) {
        arguments.setAltSpeedEnabled(enabled);
        return this;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
