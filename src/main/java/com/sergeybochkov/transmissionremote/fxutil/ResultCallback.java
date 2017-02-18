package com.sergeybochkov.transmissionremote.fxutil;

import java.io.IOException;

public interface ResultCallback {

    interface Callback {
        void call(Object object) throws IOException;
    }

    Target callback(ResultCallback.Callback callback);
}
