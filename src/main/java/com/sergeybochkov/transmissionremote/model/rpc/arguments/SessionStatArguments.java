package com.sergeybochkov.transmissionremote.model.rpc.arguments;

import com.google.gson.annotations.SerializedName;

public class SessionStatArguments extends Arguments {

    private Integer activeTorrentCount;
    private Integer downloadSpeed;
    private Integer pausedTorrentCount;
    private Integer torrentCount;
    private Integer uploadSpeed;
    @SerializedName("cumulative-stats")
    private Stat cumulativeStats;
    @SerializedName("current-stats")
    private Stat currentStat;

    public Integer getUploadSpeed() {
        return uploadSpeed;
    }

    public Integer getDownloadSpeed() {
        return downloadSpeed;
    }

    public Stat getCumulativeStats() {
        return cumulativeStats;
    }

    public Integer getTorrentCount() {
        return torrentCount;
    }

    public class Stat {
        private Long uploadedBytes;
        private Long downloadedBytes;
        private Integer filesAdded;
        private Integer sessionCount;
        private Integer secondsActive;

        public Double getRating() {
            return Double.longBitsToDouble(uploadedBytes) /
                    Double.longBitsToDouble(downloadedBytes);
        }
    }


}
