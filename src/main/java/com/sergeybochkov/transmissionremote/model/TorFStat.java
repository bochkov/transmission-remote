package com.sergeybochkov.transmissionremote.model;

public final class TorFStat {

    private Long bytesCompleted;
    private Integer priority;
    private Boolean wanted;

    public Integer priority() {
        return priority;
    }

    public Boolean wanted() {
        return wanted;
    }

}
