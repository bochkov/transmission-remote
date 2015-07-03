package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentSimplyArguments;

import java.util.List;

public class TorrentReannounce extends Request {

    private final String method = "torrent-reannounce";
    private TorrentSimplyArguments arguments = new TorrentSimplyArguments();

    public TorrentReannounce(List<Integer> ids) {
        arguments.setIds(ids);
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
