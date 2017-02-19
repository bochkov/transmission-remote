package com.sergeybochkov.transmissionremote.model;

import java.math.BigDecimal;

public final class Time {

    public final BigDecimal secs;

    public Time(BigDecimal secs) {
        this.secs = secs;
    }

    @Override
    public String toString() {
        long millis = secs.longValue();
        String retVal = "";
        if (millis >= 0) {
            long days = millis / 86400;
            if (days > 0)
                retVal = String.format("%s days", days);
            else {
                long hours = millis / 3600;
                long minutes = (millis % 3600) / 60;
                long seconds = millis % 60;
                retVal = String.format("%02d:%02d:%02d",
                        hours, minutes, seconds);
            }
        }
        return retVal;
    }
}
