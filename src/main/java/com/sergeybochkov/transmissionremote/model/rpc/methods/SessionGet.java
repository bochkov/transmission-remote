package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.SessionArguments;

public class SessionGet extends Request {

    private final String method = "session-get";

    private SessionArguments arguments = new SessionArguments();

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
