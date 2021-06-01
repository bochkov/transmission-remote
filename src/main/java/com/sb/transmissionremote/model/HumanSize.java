package com.sb.transmissionremote.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class HumanSize {

    private static final String[] SIZES = {"B", "kB", "MB", "GB", "TB"};

    private final double size;

    @Override
    public String toString() {
        double res = size;
        var count = 0;
        while (res >= 1000) {
            res /= 1000;
            ++count;
        }
        return String.format("%.2f %s", res, SIZES[count]);
    }
}
