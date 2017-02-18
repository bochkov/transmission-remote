package com.sergeybochkov.transmissionremote.fxutil;

public interface CallbackTarget {

    interface Callback {
        void call();
    }

    Target callback(CallbackTarget.Callback callback);
}
