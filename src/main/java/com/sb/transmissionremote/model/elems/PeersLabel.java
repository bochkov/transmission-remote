package com.sb.transmissionremote.model.elems;

import com.sb.transmissionremote.model.HumanSpeed;

public final class PeersLabel {

    private final int peersSendingToUs;
    private final int peersGettingFromUs;
    private final int peersConnected;
    private final double rateDownload;
    private final double rateUpload;

    public PeersLabel(int sendToUs, int getFromUs, int connected, double rateDownload, double rateUpload) {
        this.peersSendingToUs = sendToUs;
        this.peersGettingFromUs = getFromUs;
        this.peersConnected = connected;
        this.rateDownload = rateDownload;
        this.rateUpload = rateUpload;
    }

    public String downDesc() {
        return String.format(
                "Downloading from %d of %d peers — ↓ %s ↑ %s",
                peersSendingToUs,
                peersConnected,
                new HumanSpeed(rateDownload),
                new HumanSpeed(rateUpload)
        );
    }

    public String upDesc() {
        return String.format(
                "Seeding to %d of %d peers — ↑ %s",
                peersGettingFromUs,
                peersConnected,
                new HumanSpeed(rateUpload)
        );
    }

}
