package com.sergeybochkov.transmissionremote.model.rpc.arguments;

import com.google.gson.annotations.SerializedName;

public class FreeSpaceArguments extends Arguments {

    private String path;
    @SerializedName("size-bytes")
    private Long sizeBytes;

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
