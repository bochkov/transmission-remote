package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.SessionStatArguments;

public class SessionStats extends Request {

    private final String method = "session-stats";

    private SessionStatArguments arguments = new SessionStatArguments();

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
