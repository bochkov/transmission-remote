package com.sergeybochkov.transmissionremote.model.rpc.arguments;

public abstract class Arguments {

    private String result;
    private Integer tag;

    public boolean isSuccess(){
        return result.equals("success");
    }
}
