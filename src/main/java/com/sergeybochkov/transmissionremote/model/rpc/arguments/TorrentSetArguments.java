package com.sergeybochkov.transmissionremote.model.rpc.arguments;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TorrentSetArguments extends Arguments {

    private Double bandwidthPriority;
    private Double downloadLimit;
    private Boolean downloadLimited;
    @SerializedName("files-wanted")
    private List<Integer> filesWanted;
    @SerializedName("files-unwanted")
    private List<Integer> filesUnwanted;
    private Boolean honorsSessionLimits;
    private List<Integer> ids;
    private String location;
    @SerializedName("peer-limit")
    private Double peerLimit;
    @SerializedName("priority-high")
    private List<Integer> priorityHigh;
    @SerializedName("priority-low")
    private List<Integer> priorityLow;
    @SerializedName("priority-normal")
    private List<Integer> priorityNormal;
    private Double queuePosition;
    private Double seedIdleLimit;
    private Double seedIdleMode;
    private Double seedRatioLimit;
    private Double seedRatioMode;
    private List<String> trackerAdd;
    private List<?> trackerRemove;
    private List<?> trackerReplace;
    private Double uploadLimit;
    private Boolean uploadLimited;

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public void setFilesWanted(List<Integer> ids) {
        filesWanted = ids;
    }

    public void setFilesUnwanted(List<Integer> ids) {
        filesUnwanted = ids;
    }

    public void setPriorityLow(List<Integer> ids) {
        priorityLow = ids;
    }

    public void setPriorityNormal(List<Integer> ids) {
        priorityNormal = ids;
    }

    public void setPriorityHigh(List<Integer> ids) {
        priorityHigh = ids;
    }
}
