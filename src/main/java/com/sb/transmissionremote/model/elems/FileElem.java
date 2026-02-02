package com.sb.transmissionremote.model.elems;

import cordelia.jsonrpc.res.RsTorrentGet;
import sb.bdev.text.HumanSize;
import sb.bdev.text.HumanTime;

import javax.swing.*;
import java.awt.*;

public final class FileElem implements Element {

    private final RsTorrentGet.Status status;
    private final Long sizeWhenDone;
    private final Double percentDone;
    private final Double uploadRatio;
    private final Long eta;

    public FileElem(RsTorrentGet.Torrents torrents) {
        this.status = torrents.getStatus();
        this.sizeWhenDone = torrents.getSizeWhenDone();
        this.percentDone = torrents.getPercentDone();
        this.uploadRatio = torrents.getUploadRatio();
        this.eta = torrents.getEta();
    }

    @Override
    public JComponent graphic() {
        var fileLabel = new JLabel();
        fileLabel.setFont(fileLabel.getFont().deriveFont(11.0f));
        fileLabel.setForeground(Color.GRAY);
        var total = new HumanSize(sizeWhenDone * percentDone, HumanSize.US, 2);
        switch (status) {
            case DOWNLOADING -> fileLabel.setText(
                    String.format("%s of %s — %s remaining", total, new HumanSize(sizeWhenDone, HumanSize.US, 2), new HumanTime(eta * 1000))
            );
            case SEEDING -> fileLabel.setText(
                    String.format("%s, uploaded %s (Ratio %.2f)", total, new HumanSize(sizeWhenDone * uploadRatio, HumanSize.US, 2), uploadRatio)
            );
            case VERIFYING -> fileLabel.setText(
                    String.format("%s of %s", total, new HumanSize(sizeWhenDone, HumanSize.US, 2))
            );
            default -> {
                // do nothing
            }
        }
        return fileLabel;
    }
}
