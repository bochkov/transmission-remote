package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentSetArguments;

import java.util.List;


public class TorrentSet extends Request {

    private final String method = "torrent-set";
    private TorrentSetArguments arguments = new TorrentSetArguments();

    public TorrentSet() {
        this(null);
    }

    public TorrentSet(List<Integer> ids) {
        arguments.setIds(ids);
    }

    public TorrentSet setWanted(List<Integer> ids) {
        arguments.setFilesWanted(ids);
        return this;
    }

    public TorrentSet setUnwanted(List<Integer> ids) {
        arguments.setFilesUnwanted(ids);
        return this;
    }

    public TorrentSet setLowPriority(List<Integer> ids) {
        arguments.setPriorityLow(ids);
        return this;
    }

    public TorrentSet setNormalPriority(List<Integer> ids) {
        arguments.setPriorityNormal(ids);
        return this;
    }

    public TorrentSet setHighPriority(List<Integer> ids) {
        arguments.setPriorityHigh(ids);
        return this;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
