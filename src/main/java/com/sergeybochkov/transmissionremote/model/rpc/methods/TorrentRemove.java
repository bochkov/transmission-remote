package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentRemoveArguments;

import java.util.List;

public class TorrentRemove extends Request {

    private final String method = "torrent-remove";
    private TorrentRemoveArguments arguments = new TorrentRemoveArguments();

    public TorrentRemove(List<Integer> ids) {
        this(ids, false);
    }

    public TorrentRemove(List<Integer> ids, Boolean remove) {
        arguments.setIds(ids);
        arguments.setDeleteLocalData(remove);
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
