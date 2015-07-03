package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.QueueArguments;

public class QueueMoveBottom extends Request {

    private final String method = "queue-move-bottom";
    private QueueArguments arguments = new QueueArguments();

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
