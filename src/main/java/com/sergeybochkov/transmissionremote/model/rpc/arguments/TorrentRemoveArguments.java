package com.sergeybochkov.transmissionremote.model.rpc.arguments;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TorrentRemoveArguments extends Arguments {

    private List<Integer> ids;
    @SerializedName("delete-local-data")
    private Boolean deleteLocalData;

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public void setDeleteLocalData(Boolean deleteLocalData) {
        this.deleteLocalData = deleteLocalData;
    }
}
