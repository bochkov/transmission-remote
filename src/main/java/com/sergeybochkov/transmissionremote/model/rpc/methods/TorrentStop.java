package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentSimplyArguments;

import java.util.List;

public class TorrentStop extends Request {

    private final String method = "torrent-stop";
    private TorrentSimplyArguments arguments = new TorrentSimplyArguments();

    public TorrentStop(List<Integer> ids) {
        arguments.setIds(ids);
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
