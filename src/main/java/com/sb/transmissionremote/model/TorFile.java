package com.sb.transmissionremote.model;

public final class TorFile {

    private Long bytesCompleted;
    private Long length;
    private String name;

    public String name() {
        return name;
    }

    public Long completed() {
        return bytesCompleted;
    }

    public Long length() {
        return length;
    }
}
