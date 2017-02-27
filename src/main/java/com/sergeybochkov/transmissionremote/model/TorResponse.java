package com.sergeybochkov.transmissionremote.model;

import java.io.Serializable;
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
}
