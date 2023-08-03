package com.sb.transmissionremote.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.*;

import cordelia.rpc.types.Torrents;

public final class TorrentListModel extends AbstractListModel<Torrents> {

    private final List<Torrents> torrents = new ArrayList<>();

    public List<Torrents> getAll() {
        return new ArrayList<>(torrents);
    }

    @Override
    public int getSize() {
        return torrents.size();
    }

    @Override
    public Torrents getElementAt(int index) {
        return torrents.get(index);
    }

    public void clear() {
        int size = getSize();
        torrents.clear();
        fireContentsChanged(this, 0, size);
    }

    public void addAll(Collection<Torrents> newTor) {
        torrents.addAll(newTor);
        fireContentsChanged(this, 0, getSize());
    }
}
