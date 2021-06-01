package com.sb.transmissionremote.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class HumanTime {

    private final Long secs;

    @Override
    public String toString() {
        long millis = secs;
        if (millis >= 0) {
            long days = millis / 86400;
            if (days > 0)
                return String.format("%s days", days);
            else {
                long hours = millis / 3600;
                long minutes = (millis % 3600) / 60;
                long seconds = millis % 60;
                return String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }
        }
        return "";
    }
}
