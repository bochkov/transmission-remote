package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentSetLocationArguments;

public class TorrentSetLocation extends Request {

    private final String method = "torrent-set-location";
    private TorrentSetLocationArguments arguments = new TorrentSetLocationArguments();

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
