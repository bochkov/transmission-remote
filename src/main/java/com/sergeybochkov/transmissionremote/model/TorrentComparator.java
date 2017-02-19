package com.sergeybochkov.transmissionremote.model;

import java.util.Comparator;

public class TorrentComparator implements Comparator<Torrent> {
    @Override
    public int compare(Torrent o1, Torrent o2) {
        return Integer.compare(o1.id(), o2.id());
    }
}
