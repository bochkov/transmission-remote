package com.sergeybochkov.transmissionremote.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public final class TorrentResponse implements Serializable {

    private String result;
    private Map<String, List<Torrent>> arguments;

    public List<Torrent> torrents() {
        return arguments.get("torrents");
    }

}