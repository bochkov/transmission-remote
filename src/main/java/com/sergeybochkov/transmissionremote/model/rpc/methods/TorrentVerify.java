package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentSimplyArguments;

public class TorrentVerify extends Request {

    private final String method = "torrent-verify";

    private TorrentSimplyArguments arguments = new TorrentSimplyArguments();

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
