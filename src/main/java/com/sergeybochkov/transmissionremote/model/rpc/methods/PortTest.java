package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.PortTestArguments;

public class PortTest extends Request {

    private final String method = "port-test";
    private PortTestArguments arguments = new PortTestArguments();

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
