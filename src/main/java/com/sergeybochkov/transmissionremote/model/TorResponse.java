package com.sergeybochkov.transmissionremote.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class TorResponse implements Serializable {

    private static final String TORRENT_KEY = "torrents";

    private final transient Map<String, List<Tor>> arguments;

    public TorResponse(Map<String, List<Tor>> args) {
        this.arguments = args;
    }

    public List<Tor> torrents() {
        return arguments.get(TORRENT_KEY);
    }

    public List<Peer> peers() {
        return arguments.get(TORRENT_KEY).get(0).peers();
    }

    public String name() {
        return arguments.get(TORRENT_KEY).get(0).name();
    }

    public List<TorWrap> files() {
        List<TorFile> files = arguments.get(TORRENT_KEY).get(0).files();
        List<TorFStat> stats = arguments.get(TORRENT_KEY).get(0).fstats();
        List<TorWrap> res = new ArrayList<>();
        for (int i = 0; i < files.size(); ++i) {
            res.add(new TorWrap(i, files.get(i), stats.get(i)));
        }
        res.sort(Comparator.comparingInt(TorWrap::id));
        return res;
    }
}
