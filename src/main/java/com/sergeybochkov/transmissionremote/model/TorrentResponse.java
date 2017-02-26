package com.sergeybochkov.transmissionremote.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public final class TorrentResponse implements Serializable {

    private String result;
    private Map<String, List<Tr>> arguments;

    public List<Tr> torrents() {
        return arguments.get("torrents");
    }

    public List<Peer> peers() {
        return arguments.get("torrents").get(0).peers();
    }

}
