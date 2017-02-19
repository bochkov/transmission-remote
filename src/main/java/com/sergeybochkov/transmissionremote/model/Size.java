package com.sergeybochkov.transmissionremote.model;

import java.math.BigDecimal;

public final class Size {

    private final BigDecimal size;

    public Size(BigDecimal size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return size.toPlainString();
    }
}
