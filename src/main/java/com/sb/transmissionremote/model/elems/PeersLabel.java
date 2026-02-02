package com.sb.transmissionremote.model.elems;

import lombok.RequiredArgsConstructor;
import sb.bdev.text.HumanSpeed;

@RequiredArgsConstructor
public final class PeersLabel {

    private final int peersSendingToUs;
    private final int peersGettingFromUs;
    private final int peersConnected;
    private final double rateDownload;
    private final double rateUpload;

    public String downDesc() {
        return String.format(
                "Downloading from %d of %d peers — ↓ %s ↑ %s",
                peersSendingToUs,
                peersConnected,
                new HumanSpeed(rateDownload, HumanSpeed.US, 2),
                new HumanSpeed(rateUpload, HumanSpeed.US, 2)
        );
    }

    public String upDesc() {
        return String.format(
                "Seeding to %d of %d peers — ↑ %s",
                peersGettingFromUs,
                peersConnected,
                new HumanSpeed(rateUpload, HumanSpeed.US, 2)
        );
    }

}
