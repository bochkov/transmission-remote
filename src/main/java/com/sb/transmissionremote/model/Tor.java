package com.sb.transmissionremote.model;

import java.util.List;
import javax.swing.*;

import com.sb.transmissionremote.model.elems.*;
import net.miginfocom.swing.MigLayout;

public final class Tor {

    public static final int STATUS_PAUSED = 0;
    public static final int STATUS_CHECKED = 2;
    public static final int STATUS_QUEUED = 3;
    public static final int STATUS_DOWNLOAD = 4;
    public static final int STATUS_UPLOAD = 6;

    private Integer id;
    private Integer error;
    private String errorString;
    private Long eta;
    private String name;
    private Integer peersConnected;
    private Integer peersGettingFromUs;
    private Integer peersSendingToUs;
    private Double percentDone;
    private Double rateDownload;
    private Double rateUpload;
    private Long sizeWhenDone;
    private Integer status;
    private Double uploadRatio;
    private List<Peer> peers;
    private List<TorFile> files;
    private List<TorFStat> fileStats;

    public Integer id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Boolean completed() {
        return percentDone >= 1.0;
    }

    public List<Peer> peers() {
        return peers;
    }

    public List<TorFile> files() {
        return files;
    }

    public List<TorFStat> fstats() {
        return fileStats;
    }

    public JComponent graphic() {
        JPanel panel = new JPanel(new MigLayout("fillx, debug, wrap 1, insets 3 10 3 10, gap 1", "fill, grow"));
        panel.add(new ImageElem(name).graphic(), "dock west");
        panel.add(new NameElem(name).graphic());
        panel.add(new PeersSpeedElem(
                error, errorString, status, new PeersLabel(peersSendingToUs, peersGettingFromUs, peersConnected, rateDownload, rateUpload)
        ).graphic());
        panel.add(new ProgressElem(error, status, percentDone).graphic());
        panel.add(new FileElem(status, sizeWhenDone, percentDone, uploadRatio, eta).graphic());
        return panel;
    }
}
