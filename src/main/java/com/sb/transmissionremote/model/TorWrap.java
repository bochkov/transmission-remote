package com.sb.transmissionremote.model;

public final class TorWrap {

    private final int id;
    private final TorFile file;
    private final TorFStat stat;

    public TorWrap(int id, TorFile file, TorFStat stat) {
        this.id = id;
        this.file = file;
        this.stat = stat;
    }

    public int id() {
        return id;
    }

    public String name() {
        return file.name();
    }

}
