package com.sergeybochkov.transmissionremote.model.rpc.methods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sergeybochkov.transmissionremote.helpers.Settings;
import com.sergeybochkov.transmissionremote.model.rpc.arguments.Arguments;

public abstract class Request {

    private transient Gson gson = new GsonBuilder().create();

    private String result;
    private Integer tag;

    public Request fromJson(String json) {
        if (Settings.debug)
            System.out.println(json);
        return gson.fromJson(json, this.getClass());
    }

    public String toJson() {
        if (Settings.debug)
            System.out.println(gson.toJson(this));
        return gson.toJson(this);
    }

    public boolean isSuccess() {
        return result.equals("success");
    }

    public abstract Arguments getArguments();
}
