package com.sb.transmissionremote.ui;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sb.transmissionremote.AppProps;
import com.sb.transmissionremote.TransmissionRemote;
import com.sb.transmissionremote.action.*;
import com.sb.transmissionremote.model.TorrentsRender;
import com.sb.transmissionremote.scheduled.FreeSpaceSchedule;
import com.sb.transmissionremote.scheduled.SessionSchedule;
import com.sb.transmissionremote.scheduled.TorrentSchedule;
import com.sb.transmissionremote.util.Drop;
import cordelia.client.TrClient;
import cordelia.client.TypedResponse;
import cordelia.rpc.RqSessionGet;
import cordelia.rpc.RsFreeSpace;
import cordelia.rpc.RsSessionGet;
import cordelia.rpc.RsSessionStats;
import cordelia.rpc.types.Torrents;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import sb.bdev.text.HumanSize;
import sb.bdev.text.HumanSpeed;
import sb.bdev.ui.swing.HMenu;
import sb.bdev.ui.swing.HMenuBar;
import sb.bdev.ui.swing.HMenuItem;

@Slf4j
public final class FrmMain extends JFrame implements ListSelectionListener {

    private static final ScheduledExecutorService SCHEDULE = Executors.newScheduledThreadPool(5);

    private final AtomicReference<TrClient> trclient = new AtomicReference<>();
    private final AtomicReference<RsSessionGet> session = new AtomicReference<>();

    private final TorrentListModel torrents = new TorrentListModel();
    private final JList<Torrents> torList = new JList<>(torrents);

    private final Action acStartAll = new AcStartAll(trclient, this::allIds);
    private final Action acStopAll = new AcStopAll(trclient, this::allIds);
    private final Action acStartOne = new AcStartOne(trclient, this::selectedIds);
    private final Action acStopOne = new AcStopOne(trclient, this::selectedIds);
    private final Action acSpeedLimit = new AcSpeedLimit(trclient);
    private final Action acTrash = new AcRemove(trclient, this::selectedIds);
    private final Action acInfo = new AcInfo();
    private final Action acReannounce = new AcReannounce(trclient, this::selectedIds);
    private final Action acDelete = new AcRemoveData(trclient, this::selectedIds);

    private final JToggleButton speedLimitButton = new JToggleButton(acSpeedLimit);
    private final JLabel freeSpaceLabel = new JLabel("");
    private final JLabel downSpeedLabel = new JLabel("");
    private final JLabel upSpeedLabel = new JLabel("");

    private final List<ScheduledFuture<?>> tasks = new ArrayList<>();

    public FrmMain() {
        setTitle(TransmissionRemote.APP_NAME);
        setIconImage(TransmissionRemote.LOGO.getImage());
        setLayout(new MigLayout("wrap 1, insets 10, gap 7, fillx", "center", "[][fill,grow][]"));

        int metaKey = AppProps.isMacOs() ?
                InputEvent.META_DOWN_MASK :
                InputEvent.CTRL_DOWN_MASK;
        setJMenuBar(
                new HMenuBar(
                        new HMenu("File",
                                new HMenuItem(new AcChangeSession(this, this::restart), KeyStroke.getKeyStroke(KeyEvent.VK_N, metaKey)),
                                new HMenuItem(new AcAddTorrent(this, trclient), KeyStroke.getKeyStroke(KeyEvent.VK_D, metaKey)),
                                new HMenuItem(HMenu.SEPARATOR),
                                new HMenuItem(new AcExit(this))
                        ),
                        new HMenu("Edit",
                                new HMenuItem(acStartAll),
                                new HMenuItem(acStopAll),
                                new HMenuItem(HMenu.SEPARATOR),
                                new HMenuItem(acStartOne),
                                new HMenuItem(acStartOne),
                                new HMenuItem(HMenu.SEPARATOR),
                                new HMenuItem(acInfo),
                                new HMenuItem(acReannounce),
                                new HMenuItem(HMenu.SEPARATOR),
                                new HMenuItem(acTrash),
                                new HMenuItem(acDelete)
                        ),
                        new HMenu("Help",
                                new HMenuItem(new AcAbout(this))
                        )
                )
        );

        var cmdPanel = new JPanel(new MigLayout("insets 0"));
        cmdPanel.add(new JButton(acStartAll));
        cmdPanel.add(new JButton(acStopAll));
        cmdPanel.add(speedLimitButton);
        cmdPanel.add(new JButton(acTrash));
        cmdPanel.add(new JButton(acInfo));
        add(cmdPanel);

        torList.addListSelectionListener(this);
        torList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = torList.locationToIndex(e.getPoint());
                if (row >= 0 && torList.getCellBounds(row, row).contains(e.getPoint()))
                    torList.addSelectionInterval(row, row);
                else
                    torList.removeSelectionInterval(0, torrents.getSize());
            }
        });
        torList.setDropMode(DropMode.ON);
        torList.setTransferHandler(new Drop(trclient));
        torList.setCellRenderer(new TorrentsRender());
        torList.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        var sc = new JScrollPane(torList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(sc, "grow");
        JPopupMenu popup = new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                int row = torList.locationToIndex(new Point(x, y));
                Rectangle rect = torList.getCellBounds(row, row);
                if (rect != null && rect.contains(new Point(x, y)))
                    torList.addSelectionInterval(row, row);
                else
                    torList.removeSelectionInterval(0, torrents.getSize());
                super.show(invoker, x, y);
            }
        };
        popup.add(acStartOne);
        popup.add(acStopOne);
        popup.add(new JSeparator());
        popup.add(acReannounce);
        popup.add(acInfo);
        popup.add(new JSeparator());
        popup.add(acTrash);
        popup.add(acDelete);
        torList.setComponentPopupMenu(popup);

        var infoPanel = new JPanel(new MigLayout("insets 0", "[][]30[][]30[][]"));
        infoPanel.add(new JLabel(TransmissionRemote.ICON_HDD));
        infoPanel.add(freeSpaceLabel);
        freeSpaceLabel.setToolTipText("Free space left");
        infoPanel.add(new JLabel(TransmissionRemote.ICON_ARROW_DOWN));
        infoPanel.add(downSpeedLabel);
        downSpeedLabel.setToolTipText("Download speed");
        infoPanel.add(new JLabel(TransmissionRemote.ICON_ARROW_UP));
        infoPanel.add(upSpeedLabel);
        upSpeedLabel.setToolTipText("Upload speed");
        add(infoPanel);

        setMinimumSize(new Dimension(TransmissionRemote.MIN_WIDTH, TransmissionRemote.MIN_HEIGHT));
        int height = AppProps.getInt(AppProps.WINDOW_HEIGHT, TransmissionRemote.MIN_HEIGHT);
        int width = AppProps.getInt(AppProps.WINDOW_WIDTH, TransmissionRemote.MIN_HEIGHT);
        setPreferredSize(new Dimension(width, height));
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            start();
        } else {
            trclient.get().shutdown();
        }
    }

    private void start() {
        trclient.set(
                new TrClient(
                        AppProps.get(AppProps.TRANSMISSION_URL),
                        AppProps.get(AppProps.TRANSMISSION_USER),
                        AppProps.get(AppProps.TRANSMISSION_PASS)
                )
        );
        // первоначальный запрос установок сессии, значения отсюда будут использованы ниже
        TypedResponse<RsSessionGet> rsSession = trclient.get().execute(new RqSessionGet());
        this.session.set(rsSession.getArgs());

        tasks.add(
                SCHEDULE.scheduleAtFixedRate(
                        new TorrentSchedule(trclient, this::updateTorrents),
                        0L,
                        TransmissionRemote.TORRENT_INTERVAL,
                        TimeUnit.MILLISECONDS
                )
        );
        tasks.add(
                SCHEDULE.scheduleAtFixedRate(
                        new SessionSchedule(trclient, this::updateSession, this::updateSessionStats),
                        0L,
                        TransmissionRemote.SESSION_INTERVAL,
                        TimeUnit.MILLISECONDS
                )
        );
        tasks.add(
                SCHEDULE.scheduleAtFixedRate(
                        new FreeSpaceSchedule(trclient, session, this::updateFreeSpace),
                        0L,
                        TransmissionRemote.FREE_SPACE_INTERVAL,
                        TimeUnit.MILLISECONDS
                )
        );
        torList.removeSelectionInterval(0, torrents.getSize());
    }

    private void updateTorrents(List<Torrents> tors) {
        for (Torrents tor : tors) {
            LOG.debug("{}", tor.getName());
        }
        int[] selected = torList.getSelectedIndices();
        torrents.clear();
        tors.sort(Comparator.comparing(Torrents::getId));
        torrents.addAll(tors);
        for (int idx : selected) {
            torList.getSelectionModel().addSelectionInterval(idx, idx);
        }
        long completed = torrents.getAll().stream().filter(t -> t.getPercentDone() >= 1.0).count();
        LOG.debug("completed={}", completed);
        if (Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_BADGE_NUMBER)) {
            Taskbar.getTaskbar().setIconBadge(completed == 0 ? null : String.valueOf(completed));
        }
    }

    private void updateSession(RsSessionGet session) {
        setTitle(String.format("%s - [%s] - %s", TransmissionRemote.APP_NAME, AppProps.serverUrl(), session.getVersion()));
        speedLimitButton.setSelected(session.getAltSpeedEnabled());
        speedLimitButton.setIcon(speedLimitButton.isSelected() ? TransmissionRemote.ICON_ANCHOR : TransmissionRemote.ICON_ROCKET);
        speedLimitButton.setToolTipText(String.format("Now speed limit is %S", speedLimitButton.isSelected() ? "on" : "off"));
    }

    private void updateSessionStats(RsSessionStats stats) {
        upSpeedLabel.setText(new HumanSpeed(stats.getUploadSpeed(), HumanSpeed.US, 2).toString());
        downSpeedLabel.setText(new HumanSpeed(stats.getDownloadSpeed(), HumanSpeed.US, 2).toString());
    }

    private void updateFreeSpace(RsFreeSpace freeSpace) {
        freeSpaceLabel.setText(new HumanSize(freeSpace.getSizeBytes(), HumanSize.US, 2).toString());
    }

    private void restart() {
        tasks.forEach(t -> t.cancel(false));
        tasks.clear();
        setTitle(TransmissionRemote.APP_NAME);
        freeSpaceLabel.setText("");
        downSpeedLabel.setText("");
        upSpeedLabel.setText("");
        start();
    }

    private List<Object> allIds() {
        return torrents.getAll()
                .stream()
                .map(t -> (Object) t.getId())
                .toList();
    }

    private List<Object> selectedIds() {
        return torList.getSelectedValuesList()
                .stream()
                .map(t -> (Object) t.getId())
                .toList();
    }

    private void setActionsEnabled(boolean on) {
        acStartOne.setEnabled(on);
        acStopOne.setEnabled(on);
        acReannounce.setEnabled(on);
        acInfo.setEnabled(on);
        acTrash.setEnabled(on);
        acDelete.setEnabled(on);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        setActionsEnabled(!torList.getSelectedValuesList().isEmpty());
    }
}
