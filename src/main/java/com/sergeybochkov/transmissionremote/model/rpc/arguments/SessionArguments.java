package com.sergeybochkov.transmissionremote.model.rpc.arguments;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SessionArguments extends Arguments {

    @SerializedName("alt-speed-down")
    private Integer altSpeedDown;
    @SerializedName("alt-speed-enabled")
    private Boolean altSpeedEnabled;
    @SerializedName("alt-speed-time-begin")
    private Integer altSpeedTimeBegin;
    @SerializedName("alt-speed-time-enabled")
    private Boolean altSpeedTimeEnabled;
    @SerializedName("alt-speed-time-end")
    private Integer altSpeedTimeEnd;
    @SerializedName("alt-speed-time-day")
    private Integer altSpeedTimeDay;
    @SerializedName("alt-speed-up")
    private Integer altSpeedUp;
    @SerializedName("blocklist-url")
    private String blocklistUrl;
    @SerializedName("blocklist-enabled")
    private Boolean blocklistEnabled;
    @SerializedName("blocklist-size")
    private Integer blocklistSize;
    @SerializedName("cache-size-mb")
    private Integer cacheSizeMb;
    @SerializedName("config-dir")
    private String configDir;
    @SerializedName("download-dir")
    private String downloadDir;
    @SerializedName("download-queue-size")
    private Integer downloadQueueSize;
    @SerializedName("download-queue-enabled")
    private Boolean downloadQueueEnabled;
    @SerializedName("dht-enabled")
    private Boolean dhtEnabled;
    private String encryption;
    @SerializedName("idle-seeding-limit")
    private Integer idleSeedingLimit;
    @SerializedName("idle-seeding-limit-enabled")
    private Boolean idleSeedingLimitEnabled;
    @SerializedName("incomplete-dir")
    private String incompleteDir;
    @SerializedName("incomplete-dir-enabled")
    private Boolean incompleteDirEnabled;
    @SerializedName("lpd-enabled")
    private Boolean lpdEnabled;
    @SerializedName("peer-limit-global")
    private Integer peerLimitGlobal;
    @SerializedName("peer-limit-per-torrent")
    private Integer peerLimitPerTorrent;
    @SerializedName("pex-enabled")
    private Boolean pexEnabled;
    @SerializedName("peer-port")
    private Integer peerPort;
    @SerializedName("peer-port-random-on-start")
    private Boolean peerPortRandomOnStart;
    @SerializedName("port-forwarding-enabled")
    private Boolean portForwardingEnabled;
    @SerializedName("queue-stalled-enabled")
    private Boolean queueStalledEnabled;
    @SerializedName("queue-stalled-minutes")
    private Integer queueStalledMinutes;
    @SerializedName("rename-partial-files")
    private Boolean renamePartialFiles;
    @SerializedName("model-version")
    private Integer rpcVersion;
    @SerializedName("model-version-minimum")
    private Integer rpcVersionMinimum;
    @SerializedName("script-torrent-done-file")
    private String scriptTorrentDoneFile;
    @SerializedName("script-torrent-done-enabled")
    private Boolean scriptTorrentDoneEnabled;
    private Float seedRatioLimit;
    private Boolean seedRatioLimited;
    @SerializedName("seed-queue-size")
    private Integer seedQueueSize;
    @SerializedName("seed-queue-enabled")
    private Boolean seedQueueEnabled;
    @SerializedName("speed-limit-down")
    private Integer speedLimitDown;
    @SerializedName("speed-limit-down-enabled")
    private Boolean speedLimitDownEnabled;
    @SerializedName("speed-limit-up")
    private Integer speedLimitUp;
    @SerializedName("speed-limit-up-enabled")
    private Boolean speedLimitUpEnabled;
    @SerializedName("start-added-torrents")
    private Boolean startAddedTorrents;
    @SerializedName("trash-original-torrent-files")
    private Boolean trashOriginalTorrentFiles;
    private Unit units;
    @SerializedName("utp-enabled")
    private Boolean utpEnabled;
    private String version;

    public Boolean getAltSpeedEnabled() {
        return altSpeedEnabled;
    }

    public void setAltSpeedEnabled(boolean enabled) {
        altSpeedEnabled = enabled;
    }

    public String getVersion() {
        return version;
    }

    public String getDownloadDir() {
        return downloadDir;
    }

    private class Unit {

        @SerializedName("speed-units")
        private List<String> speedUnits;
        @SerializedName("speed-bytes")
        private Integer speedBytes;
        @SerializedName("size-units")
        private List<String> sizeUnits;
        @SerializedName("size-bytes")
        private Integer sizeBytes;
        @SerializedName("memory-units")
        private List<String> memoryUnits;
        @SerializedName("memory-bytes")
        private Integer memoryBytes;
    }

}
