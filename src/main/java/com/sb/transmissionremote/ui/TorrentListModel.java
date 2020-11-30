package com.sb.transmissionremote.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.*;

import com.sb.transmissionremote.model.Tor;

public final class TorrentListModel extends AbstractListModel<Tor> {

    private final List<Tor> torrents = new ArrayList<>();

    public List<Tor> getAll() {
        return new ArrayList<>(torrents);
    }

    @Override
    public int getSize() {
        return torrents.size();
    }

    @Override
    public Tor getElementAt(int index) {
        return torrents.get(index);
    }

    public void clear() {
        int size = getSize();
        torrents.clear();
        fireContentsChanged(this, 0, size);
    }

    public void addAll(Collection<Tor> newtor) {
        torrents.addAll(newtor);
        fireContentsChanged(this, 0, getSize());
    }
}
