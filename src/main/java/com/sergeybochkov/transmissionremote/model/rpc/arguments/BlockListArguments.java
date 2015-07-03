package com.sergeybochkov.transmissionremote.model.rpc.arguments;

import com.google.gson.annotations.SerializedName;

public class BlockListArguments extends Arguments {

    @SerializedName("blocklist-size")
    private Integer blocklistSize;
}
