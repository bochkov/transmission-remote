package com.sergeybochkov.transmissionremote.model;

import java.math.BigDecimal;

public final class Size {

    private static final String[] SIZES = {"B", "kB", "MB", "GB", "TB"};

    private final BigDecimal size;

    public Size(BigDecimal size) {
        this.size = size;
    }

    @Override
    public String toString() {
        double res = size.doubleValue();
        int count = 0;
        while (res >= 1000) {
            res /= 1000;
            ++count;
        }
        return String.format("%.3f %s", res, SIZES[count]);
    }
}
