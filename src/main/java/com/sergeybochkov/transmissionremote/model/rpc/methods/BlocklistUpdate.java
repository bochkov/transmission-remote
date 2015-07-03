package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.BlockListArguments;

public class BlocklistUpdate extends Request {

    private final String method = "blocklist-update";

    private BlockListArguments arguments = new BlockListArguments();

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
