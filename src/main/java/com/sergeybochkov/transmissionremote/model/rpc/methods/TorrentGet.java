package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentGetArguments;

import java.util.List;

public class TorrentGet extends Request {

    private final String method = "torrent-get";
    TorrentGetArguments arguments = new TorrentGetArguments();

    public TorrentGet() {
        this(null);
    }

    public TorrentGet(List<Integer> ids) {
        arguments.setIds(ids);
    }

    public TorrentGet setFields(List<String> fields) {
        arguments.setFields(fields);
        return this;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
