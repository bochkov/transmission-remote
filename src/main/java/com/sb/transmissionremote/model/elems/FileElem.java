package com.sb.transmissionremote.model.elems;

import java.awt.*;
import javax.swing.*;

import com.sb.transmissionremote.model.HumanSize;
import com.sb.transmissionremote.model.HumanTime;
import com.sb.transmissionremote.model.Tor;

public final class FileElem implements Element {

    private final int status;
    private final long sizeWhenDone;
    private final double percentDone;
    private final long eta;
    private final double uploadRatio;

    public FileElem(int status, long sizeWhenDone, double percentDone, double uploadRatio, long eta) {
        this.status = status;
        this.sizeWhenDone = sizeWhenDone;
        this.percentDone = percentDone;
        this.uploadRatio = uploadRatio;
        this.eta = eta;
    }

    @Override
    public JComponent graphic() {
        JLabel fileLabel = new JLabel();
        fileLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        fileLabel.setForeground(Color.GRAY);
        HumanSize total = new HumanSize(sizeWhenDone * percentDone);
        switch (status) {
            case Tor.STATUS_DOWNLOAD:
                fileLabel.setText(String.format("%s of %s — %s remaining",
                        total, new HumanSize(sizeWhenDone), new HumanTime(eta)));
                break;
            case Tor.STATUS_UPLOAD:
                fileLabel.setText(String.format("%s, uploaded %s (Ratio %.2f)",
                        total, new HumanSize(sizeWhenDone * uploadRatio), uploadRatio));
                break;
            case Tor.STATUS_CHECKED:
                fileLabel.setText(String.format("%s of %s", total, new HumanSize(sizeWhenDone)));
                break;
            default:
                break;
        }
        return fileLabel;
    }
}
