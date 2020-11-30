package com.sb.transmissionremote.model;

public final class HumanSpeed {

    private static final String[] SPEEDS = {"B/s", "kB/s", "MB/s", "GB/s", "TB/s"};

    private final Double speed;

    public HumanSpeed(Double speed) {
        this.speed = speed;
    }

    public HumanSpeed(String speed) {
        String[] parts = speed.split(" ");
        int multy = 0;
        double value = Double.parseDouble(parts[0].replace(",", "."));
        for (String sp : SPEEDS) {
            if (parts[1].equals(sp))
                break;
            ++multy;
        }
        this.speed = multy == 0 ?
                value :
                value * multy * 1000;
    }

    public Double speed() {
        return speed;
    }

    @Override
    public String toString() {
        double res = speed;
        int count = 0;
        while (res >= 1000) {
            res /= 1000;
            ++count;
        }
        return String.format("%.2f %s", res, SPEEDS[count]);
    }
}
