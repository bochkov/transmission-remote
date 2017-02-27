package com.sergeybochkov.transmissionremote.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class TorResponse implements Serializable {

    private String result;
    private Map<String, List<Tor>> arguments;

    public List<Tor> torrents() {
        return arguments.get("torrents");
    }

    public List<Peer> peers() {
        return arguments.get("torrents").get(0).peers();
    }

    public String name() {
        return arguments.get("torrents").get(0).name();
    }

    public List<TorWrap> files() {
        List<TorFile> files = arguments.get("torrents").get(0).files();
        List<TorFStat> stats = arguments.get("torrents").get(0).fstats();
        List<TorWrap> result = new ArrayList<>();
        for (int i = 0; i < files.size(); ++i) {
            result.add(new TorWrap(i, files.get(i), stats.get(i)));
        }
        result.sort(Comparator.comparingInt(TorWrap::id));
        return result;
    }
}
