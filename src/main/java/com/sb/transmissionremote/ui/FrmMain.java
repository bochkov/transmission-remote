package com.sb.transmissionremote.ui;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sb.transmissionremote.AppProperties;
import com.sb.transmissionremote.TransmissionRemote;
import com.sb.transmissionremote.action.*;
import com.sb.transmissionremote.model.HumanSize;
import com.sb.transmissionremote.model.HumanSpeed;
import com.sb.transmissionremote.model.Tor;
import com.sb.transmissionremote.model.TorRender;
import com.sb.transmissionremote.scheduled.FreeSpaceSchedule;
import com.sb.transmissionremote.scheduled.SessionSchedule;
import com.sb.transmissionremote.scheduled.TorrentSchedule;
import com.sb.transmissionremote.util.Drop;
import cordelia.client.Client;
import cordelia.client.TrClient;
import cordelia.client.TrResponse;
import cordelia.rpc.SessionGet;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

@Slf4j
public final class FrmMain extends JFrame implements ListSelectionListener {

    private static final ScheduledExecutorService SCHEDULE = Executors.newScheduledThreadPool(5);

    private final AppProperties props = AppProperties.get();
    private final Map<String, Object> session = new HashMap<>();
    private final AtomicReference<Client> trclient = new AtomicReference<>();

    private final TorrentListModel torrents = new TorrentListModel();
    private final JList<Tor> torList = new JList<>(torrents);

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

        int metaKey = props.isMacOs() ?
                InputEvent.META_DOWN_MASK :
                InputEvent.CTRL_DOWN_MASK;
        setJMenuBar(
                new TMenuBar(
                        new TMenu("File",
                                new TMenuItem(new AcChangeSession(this, this::restart), KeyStroke.getKeyStroke(KeyEvent.VK_N, metaKey)),
                                new TMenuItem(new AcAddTorrent(this, trclient), KeyStroke.getKeyStroke(KeyEvent.VK_D, metaKey)),
                                new TMenuItem("---"),
                                new TMenuItem(new AcExit(this))
                        ),
                        new TMenu("Edit",
                                new TMenuItem(acStartAll),
                                new TMenuItem(acStopAll),
                                new TMenuItem("---"),
                                new TMenuItem(acStartOne),
                                new TMenuItem(acStartOne),
                                new TMenuItem("---"),
                                new TMenuItem(acInfo),
                                new TMenuItem(acReannounce),
                                new TMenuItem("---"),
                                new TMenuItem(acTrash),
                                new TMenuItem(acDelete)
                        ),
                        new TMenu("Help",
                                new TMenuItem(new AcAbout(this))
                        )
                )
        );

        JPanel cmdPanel = new JPanel(new MigLayout("insets 0"));
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
        torList.setCellRenderer(new TorRender());
        torList.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane sc = new JScrollPane(torList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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

        JPanel infoPanel = new JPanel(new MigLayout("insets 0", "[][]30[][]30[][]"));
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
        setPreferredSize(new Dimension(props.width(), props.height()));
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            start();
        }
    }

    private void start() {
        trclient.set(new TrClient(props.uri()));
        // первоначальный запрос установок сессии, значения отсюда будут использованы ниже
        session.putAll(
                trclient.get().post(new SessionGet(), TrResponse.class).arguments()
        );
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
                        new SessionSchedule(trclient, this::updateSession),
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

    private void updateTorrents(List<Tor> newtors) {
        for (Tor tor : newtors) {
            LOG.debug("{}", tor.name());
        }
        int[] selected = torList.getSelectedIndices();
        torrents.clear();
        newtors.sort(Comparator.comparing(Tor::id));
        torrents.addAll(newtors);
        for (int idx : selected) {
            torList.getSelectionModel().addSelectionInterval(idx, idx);
        }
        long completed = torrents.getAll().stream().filter(Tor::completed).count();
        LOG.debug("completed={}", completed);
        if (Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_BADGE_NUMBER))
            Taskbar.getTaskbar().setIconBadge(
                    completed > 0 ? String.valueOf(completed) : null
            );
    }

    private void updateSession(Map<String, Object> map) {
        setTitle(String.format("%s - [%s] - %s", TransmissionRemote.APP_NAME, props.server(), map.get("version")));
        upSpeedLabel.setText(new HumanSpeed((Double) map.get("uploadSpeed")).toString());
        downSpeedLabel.setText(new HumanSpeed((Double) map.get("downloadSpeed")).toString());
        speedLimitButton.setSelected((Boolean) map.get("alt-speed-enabled"));
        speedLimitButton.setIcon(speedLimitButton.isSelected() ? TransmissionRemote.ICON_ANCHOR : TransmissionRemote.ICON_ROCKET);
        speedLimitButton.setToolTipText(String.format("Now speed limit is %S", speedLimitButton.isSelected() ? "on" : "off"));
    }

    private void updateFreeSpace(HumanSize size) {
        freeSpaceLabel.setText(size.toString());
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

    private Object[] allIds() {
        return torrents.getAll()
                .stream()
                .map(Tor::id)
                .toArray();
    }

    private Object[] selectedIds() {
        return torList.getSelectedValuesList()
                .stream()
                .map(Tor::id)
                .toArray();
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
