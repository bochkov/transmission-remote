package com.sb.transmissionremote.model;

public final class HumanSize {

    private static final String[] SIZES = {"B", "kB", "MB", "GB", "TB"};

    private final double size;

    public HumanSize(double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        double res = size;
        int count = 0;
        while (res >= 1000) {
            res /= 1000;
            ++count;
        }
        return String.format("%.2f %s", res, SIZES[count]);
    }
}
