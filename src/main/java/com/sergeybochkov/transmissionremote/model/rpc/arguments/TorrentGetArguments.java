package com.sergeybochkov.transmissionremote.model.rpc.arguments;

import java.util.List;

public class TorrentGetArguments extends Arguments {

    private List<String> fields;
    private List<Integer> ids;
    private List<TorrentField> torrents;

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<TorrentField> getFields() {
        return torrents;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
