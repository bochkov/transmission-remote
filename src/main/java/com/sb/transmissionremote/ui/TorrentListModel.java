package com.sb.transmissionremote.ui;

import cordelia.jsonrpc.res.RsTorrentGet;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class TorrentListModel extends AbstractListModel<RsTorrentGet.Torrents> {

    private final List<RsTorrentGet.Torrents> torrents = new ArrayList<>();

    public List<RsTorrentGet.Torrents> getAll() {
        return new ArrayList<>(torrents);
    }

    @Override
    public int getSize() {
        return torrents.size();
    }

    @Override
    public RsTorrentGet.Torrents getElementAt(int index) {
        return torrents.get(index);
    }

    public void clear() {
        int size = getSize();
        torrents.clear();
        fireContentsChanged(this, 0, size);
    }

    public void addAll(Collection<RsTorrentGet.Torrents> tor) {
        torrents.addAll(tor);
        fireContentsChanged(this, 0, getSize());
    }
}
