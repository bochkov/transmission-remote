package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentRenamePathArguments;

public class TorrentRenamePath extends Request {

    private final String method = "torrent-rename-path";
    private TorrentRenamePathArguments arguments = new TorrentRenamePathArguments();

    @Override
    public Arguments getArguments() {
        return arguments;
    }
}
