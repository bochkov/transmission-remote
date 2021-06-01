package com.sb.transmissionremote.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TorWrap {

    private final int id;
    private final TorFile file;
    private final TorFStat stat;

    public int id() {
        return id;
    }

    public String name() {
        return file.name();
    }

}
