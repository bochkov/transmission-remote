package com.sb.transmissionremote.model.elems;

import java.awt.*;
import javax.swing.*;

import com.sb.transmissionremote.model.Tor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ProgressElem implements Element {

    private final int error;
    private final int status;
    private final double percentDone;

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
                case Tor.STATUS_UPLOAD:
                    pb.setForeground(new Color(5, 109, 5));
                    break;
                case Tor.STATUS_PAUSED:
                    pb.setForeground(Color.LIGHT_GRAY);
                    break;
                case Tor.STATUS_CHECKED:
                    pb.setForeground(Color.GRAY);
                    break;
                case Tor.STATUS_QUEUED:
                    pb.setForeground(Color.DARK_GRAY);
                    break;
                case Tor.STATUS_DOWNLOAD:
                default:
                    break;
            }
        }
        return pb;
    }
}
