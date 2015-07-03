package com.sergeybochkov.transmissionremote.model.rpc.arguments;

import java.util.List;

public class TorrentSimplyArguments extends Arguments {

    private List<Integer> ids;

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
