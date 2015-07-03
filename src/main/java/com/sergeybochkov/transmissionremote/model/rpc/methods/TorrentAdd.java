package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentAddArguments;

public class TorrentAdd extends Request {

    private final String method = "torrent-add";
    private TorrentAddArguments arguments = new TorrentAddArguments();

    public TorrentAdd setDownloadDir(String dir) {
        arguments.setDownloadDir(dir);
        return this;
    }

    public TorrentAdd byUrl(String url) {
        arguments.setFilename(url);
        return this;
    }

    public TorrentAdd byFile(String metainfo) {
        arguments.setMetainfo(metainfo);
        return this;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
