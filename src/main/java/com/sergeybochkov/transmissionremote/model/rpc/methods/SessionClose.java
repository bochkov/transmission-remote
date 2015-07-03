package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;

public class SessionClose extends Request {

    private final String method = "session-close";

    @Override
    public Arguments getArguments() {
        return null;
    }
}
