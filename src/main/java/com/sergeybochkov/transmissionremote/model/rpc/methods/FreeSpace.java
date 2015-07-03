package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.FreeSpaceArguments;

public class FreeSpace extends Request {

    private String method = "free-space";
    private FreeSpaceArguments arguments = new FreeSpaceArguments();

    public FreeSpace(String path) {
        arguments.setPath(path);
    }

    public FreeSpace() {
        this(".");
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
