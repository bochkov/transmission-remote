package com.sergeybochkov.transmissionremote.model.rpc.arguments;


import com.google.gson.annotations.SerializedName;

public class PortTestArguments extends Arguments {
    @SerializedName("port-is-open")
    private Boolean portIsOpen;
}
