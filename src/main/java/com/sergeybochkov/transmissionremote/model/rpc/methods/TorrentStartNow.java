package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentSimplyArguments;

public class TorrentStartNow extends Request {

    private final String method = "torrent-start-now";

    private TorrentSimplyArguments arguments = new TorrentSimplyArguments();

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
