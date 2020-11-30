package com.sb.transmissionremote;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.sb.transmissionremote.ui.FrmMain;
import com.sb.transmissionremote.util.FlatSvgFlip;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TransmissionRemote {

    public static final String APP_NAME = "Transmission Remote";
    public static final ImageIcon LOGO = new ImageIcon(TransmissionRemote.class.getResource("/style/transmission-remote.png"));

    public static final int MIN_HEIGHT = 650;
    public static final int MIN_WIDTH = 500;

    public static final int SESSION_INTERVAL = 3000;
    public static final int TORRENT_INTERVAL = 1500;
    public static final int FREE_SPACE_INTERVAL = 5000;
    public static final int INFO_INTERVAL = 1000;

    public static final FlatSVGIcon ICON_GLOBE = new FlatSVGIcon("svg/globe.svg", 12, 12);
    public static final FlatSVGIcon ICON_FOLDER_OPEN_O = new FlatSVGIcon("svg/folder-open-o.svg", 12, 12);
    public static final FlatSVGIcon ICON_FOLDER_OPEN = new FlatSVGIcon("svg/folder-open.svg", 16, 16);
    public static final FlatSVGIcon ICON_SIGN_OUT = new FlatSVGIcon("svg/sign-out-alt.svg", 12, 12);
    public static final FlatSVGIcon ICON_REPLY_ALL = new FlatSvgFlip("svg/reply-all.svg", 12, 12);
    public static final FlatSVGIcon ICON_REPLY = new FlatSVGIcon("svg/reply.svg", 12, 12);
    public static final FlatSVGIcon ICON_SQUARE = new FlatSVGIcon("svg/square-full.svg", 12, 12);
    public static final FlatSVGIcon ICON_SQUARE_O = new FlatSVGIcon("svg/square.svg", 12, 12);
    public static final FlatSVGIcon ICON_INFO_CIRCLE = new FlatSVGIcon("svg/info-circle.svg", 12, 12);
    public static final FlatSVGIcon ICON_REFRESH = new FlatSVGIcon("svg/redo.svg", 12, 12);
    public static final FlatSVGIcon ICON_TRASH = new FlatSVGIcon("svg/trash.svg", 12, 12);
    public static final FlatSVGIcon ICON_BAN = new FlatSVGIcon("svg/ban.svg", 12, 12);
    public static final FlatSVGIcon ICON_FLASK = new FlatSVGIcon("svg/flask.svg", 12, 12);
    public static final FlatSVGIcon ICON_ROCKET = new FlatSVGIcon("svg/rocket.svg", 12, 12);
    public static final FlatSVGIcon ICON_ANCHOR = new FlatSVGIcon("svg/anchor.svg", 12, 12);
    public static final FlatSVGIcon ICON_LOCK = new FlatSVGIcon("svg/lock.svg", 12, 12);
    public static final FlatSVGIcon ICON_CIRCLE = new FlatSVGIcon("svg/circle.svg", 12, 12);
    public static final FlatSVGIcon ICON_ARROW_UP = new FlatSVGIcon("svg/arrow-up.svg", 12, 12);
    public static final FlatSVGIcon ICON_ARROW_DOWN = new FlatSVGIcon("svg/arrow-down.svg", 12, 12);
    public static final FlatSVGIcon ICON_HDD = new FlatSVGIcon("svg/hdd.svg", 12, 12);
    public static final FlatSVGIcon ICON_SIGNAL = new FlatSVGIcon("svg/signal.svg", 12, 12);
    public static final FlatSVGIcon ICON_ENVELOPE_O = new FlatSVGIcon("svg/envelope-open.svg", 12, 12);
    public static final FlatSVGIcon ICON_LINK = new FlatSVGIcon("svg/link.svg", 12, 12);

    public static final AppProperties PROPS = AppProperties.get();

    private static void run() {
        JFrame mainFrame = new FrmMain();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Window frm = (Window) e.getSource();
                PROPS.setWidth(frm.getWidth());
                PROPS.setHeight(frm.getHeight());
                PROPS.store();
            }
        });
        mainFrame.setVisible(true);
    }

    @SneakyThrows
    public static void main(String[] args) {
        FlatLightLaf.install();
        TransmissionRemote.run();
    }
}
