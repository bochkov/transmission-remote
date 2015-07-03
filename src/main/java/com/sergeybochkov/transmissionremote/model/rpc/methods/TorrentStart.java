package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentSimplyArguments;

import java.util.List;

public class TorrentStart extends Request {

    private String method = "torrent-start";

    private TorrentSimplyArguments arguments = new TorrentSimplyArguments();

    public TorrentStart(List<Integer> ids) {
        arguments.setIds(ids);
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
