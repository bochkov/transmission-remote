package com.sergeybochkov.transmissionremote.model.rpc.arguments;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TorrentAddArguments extends Arguments {

    private String cookies;
    @SerializedName("download-dir")
    private String downloadDir;
    private String filename;
    private String metainfo; // base64 encoded string
    @SerializedName("peer-limit")
    private Integer peerLimit;
    private Integer bandwidthPriority;
    @SerializedName("files-wanted")
    private List<?> filesWanted;
    @SerializedName("files-unwanted")
    private List<?> filesUnwanted;
    @SerializedName("priority-high")
    private List<Integer> priorityHigh;
    @SerializedName("priority-low")
    private List<Integer> priorityLow;
    @SerializedName("priority-normal")
    private List<Integer> priorityNormal;

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setMetainfo(String metainfo) {
        this.metainfo = metainfo;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }
}
