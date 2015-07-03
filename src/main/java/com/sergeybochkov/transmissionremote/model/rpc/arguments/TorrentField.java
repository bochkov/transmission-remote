package com.sergeybochkov.transmissionremote.model.rpc.arguments;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorrentField {

    private Integer activityDate;
    private Integer addedDate;
    private Integer bandwidthPriority;
    private String comment;
    private Integer corruptEver;
    private String creator;
    private Integer dateCreated;
    private Integer desiredAvailable;
    private Integer doneDate;
    private String downloadDir;
    private Integer downloadedEver;
    private Integer downloadLimit;
    private Boolean downloadLimited;
    private Integer error;
    private String errorString;
    private Integer eta;
    private Integer etaIdle;
    private List<File> files;
    private List<FileStat> fileStats;
    private String hashString;
    private Integer haveUnchecked;
    private Integer haveValid;
    private Boolean honorsSessionLimits;
    private Integer id;
    private Boolean isFinished;
    private Boolean isPrivate;
    private Boolean isStalled;
    private Long leftUntilDone;
    private Integer magnetLink;
    private Integer manualAnnounceTime;
    private Integer maxConnectedPeers;
    private Double metadataPercentComplete;
    private String name;
    @SerializedName("peer-limit")
    private Integer peerLimit;
    private List<Peer> peers;
    private Integer peersConnected;
    private PeersFrom peersFrom;
    private Integer peersGettingFromUs;
    private Integer peersSendingToUs;
    private Double percentDone;
    private String pieces; // byte64 encoding string
    private Integer pieceCount;
    private Integer pieceSize;
    private List<Integer> priorities;
    private Integer queuePosition;
    private Integer rateDownload;
    private Integer rateUpload;
    private Double recheckProgress;
    private Integer secondsDownloading;
    private Integer secondsSeeding;
    private Integer seedIdleLimit;
    private Integer seedIdleMode;
    private Double seedRatioLimit;
    private Integer seedRatioMode;
    private Long sizeWhenDone;
    private Integer startDate;
    private Integer status;
    private List<Tracker> trackers;
    private List<TrackerStat> trackerStats;
    private Long totalSize;
    private String torrentFile;
    private Integer uploadedEver;
    private Integer uploadLimit;
    private Boolean uploadLimited;
    private Double uploadRatio;
    private List<Boolean> wanted;
    private List<Webseed> webseeds;
    private Integer webseedsSendingToUs;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getSizeWhenDone() {
        return sizeWhenDone;
    }

    public Double getPercentDone() {
        return percentDone;
    }

    public Integer getPeersConnected() {
        return peersConnected;
    }

    public Integer getPeersGettingFromUs() {
        return peersGettingFromUs;
    }

    public Integer getPeersSendingToUs() {
        return peersSendingToUs;
    }

    public Integer getRateDownload() {
        return rateDownload;
    }

    public Integer getRateUpload() {
        return rateUpload;
    }

    public Double getUploadRatio() {
        return uploadRatio;
    }

    public Integer getEta() {
        return eta;
    }

    public Integer getStatus() {
        return status;
    }

    public List<Peer> getPeers() {
        return peers;
    }

    public List<File> getFiles() {
        return files;
    }

    public List<FileStat> getFileStats() {
        return fileStats;
    }

    public Integer getError() {
        return error;
    }

    public String getErrorString() {
        return errorString;
    }

    public class File {

        private Long bytesCompleted;
        private Long length;
        private String name;

        public String getName() {
            return name;
        }

        public Long getLength() {
            return length;
        }

        public Long getBytesCompleted() {
            return bytesCompleted;
        }
    }

    public class FileStat {

        private Long bytesCompleted;
        private Boolean wanted;
        private Integer priority;

        public Boolean getWanted() {
            return wanted;
        }

        public Integer getPriority() {
            return priority;
        }

        public Long getBytesCompleted() {
            return bytesCompleted;
        }
    }

    enum PeerSort { BY_ADDRESS, BY_CLIENT_NAME, BY_RATE_TO, BY_RATE_FROM, BY_ENCRYPTION }

    public class Peer implements Comparable {

        private String address;
        private String clientName;
        private Boolean clientIsChocked;
        private Boolean clientIsInterested;
        private String flagStr;
        private Boolean isDownloadingFrom;
        private Boolean isEncrypted;
        private Boolean isIncoming;
        private Boolean isUploadingTo;
        private Boolean isUTP;
        private Boolean peersIsChocked;
        private Boolean peersIsInterested;
        private Integer port;
        private Double progress;
        private Integer rateToClient;
        private Integer rateToPeer;

        public String getAddress() {
            return address;
        }

        public String getClientName() {
            return clientName;
        }

        public Integer getRateToClient() {
            return rateToClient;
        }

        public Integer getRateToPeer() {
            return rateToPeer;
        }

        public Boolean getEncrypted() {
            return isEncrypted;
        }

        private PeerSort sort = PeerSort.BY_ADDRESS;

        public void setSorting(PeerSort sort) {
            this.sort = sort;
        }

        @Override
        public int compareTo(@NotNull Object o) {
            if (o instanceof Peer) {
                Peer peer = (Peer) o;
                System.err.println(sort);
                switch (sort) {
                    case BY_ADDRESS:
                        Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)");
                        Matcher m1 = pattern.matcher(getAddress());
                        Matcher m2 = pattern.matcher(peer.getAddress());
                        if (m1.find() && m2.find()) {
                            int c1 = Integer.valueOf(m1.group(1)).compareTo(Integer.valueOf(m2.group(1)));
                            if (c1 != 0)
                                return c1;

                            int c2 = Integer.valueOf(m1.group(2)).compareTo(Integer.valueOf(m2.group(2)));
                            if (c2 != 0)
                                return c2;

                            int c3 = Integer.valueOf(m1.group(3)).compareTo(Integer.valueOf(m2.group(3)));
                            if (c3 != 0)
                                return c3;

                            return Integer.valueOf(m1.group(4)).compareTo(Integer.valueOf(m2.group(4)));
                        }
                        return 0;
                }
            }
            return 0;
        }
    }

    private class PeersFrom {
        private Integer fromCache;
        private Integer fromDht;
        private Integer fromIncoming;
        private Integer fromLpd;
        private Integer fromLtep;
        private Integer fromPex;
        private Integer fromTracker;
    }

    private class Tracker {
        private String announce;
        private Integer id;
        private String scrape;
        private Integer tier;
    }

    private class TrackerStat {
        private String announce;
        private Integer announceState;
        private Integer downloadCount;
        private Boolean hasAnnounced;
        private Boolean hasScraped;
        private String host;
        private Integer id;
        private Boolean isBackup;
        private Integer lastAnnouncePeerCount;
        private String lastAnnounceResult;
        private Integer lastAnnounceStartTime;
        private Boolean lastAnnounceSucceeded;
        private Integer lastAnnounceTime;
        private Boolean lastAnnounceTimedOut;
        private String lastScrapeResult;
        private Integer lastScrapeStartTime;
        private Boolean lastScrapeSucceeded;
        private Integer lastScrapeTime;
        private Boolean lastScrapeTimedOut;
        private Integer leecherCount;
        private Integer nextAnnounceTime;
        private Integer nextScrapeTime;
        private String scrape;
        private Integer scrapeState;
        private Integer seederCount;
        private Integer tier;
    }

    private class Webseed {
        private String webseed;
    }

}
