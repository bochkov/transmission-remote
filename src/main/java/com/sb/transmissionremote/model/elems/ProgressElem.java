package com.sb.transmissionremote.model.elems;

import java.awt.*;
import javax.swing.*;

import cordelia.rpc.types.Status;
import cordelia.rpc.types.Torrents;

public final class ProgressElem implements Element {

    private final Long error;
    private final Status status;
    private final Double percentDone;

    public ProgressElem(Torrents t) {
        this.error = t.getError();
        this.status = t.getStatus();
        this.percentDone = t.getPercentDone();
    }

    @Override
    public JComponent graphic() {
        var pb = new JProgressBar();
        pb.setMaximum(100);
        pb.setValue((int) (percentDone * 100));
        pb.setStringPainted(true);
        pb.setString(String.format("%.2f %%", percentDone * 100));
        if (error != 0) {
            pb.setForeground(Color.RED);
        } else {
            switch (status) {
                case SEEDING -> pb.setForeground(new Color(5, 109, 5));
                case STOPPED -> pb.setForeground(Color.LIGHT_GRAY);
                case VERIFYING -> pb.setForeground(Color.GRAY);
                case QUEUED_TO_DOWNLOAD, QUEUED_TO_SEED, QUEUED_TO_VERIFY -> pb.setForeground(Color.DARK_GRAY);
                default -> {
                    // do nothing
                }
            }
        }
        return pb;
    }
}
