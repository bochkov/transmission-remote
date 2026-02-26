package com.sb.transmissionremote.ui;

import com.sb.transmissionremote.AppProps;
import com.sb.transmissionremote.TransmissionRemote;
import com.sb.transmissionremote.model.TrSource;
import com.sb.transmissionremote.model.TrSourceFile;
import com.sb.transmissionremote.model.TrSourceTrash;
import com.sb.transmissionremote.model.TrSourceUrl;
import com.sb.transmissionremote.util.AbstractDocListener;
import cordelia.client.TrClient;
import cordelia.jsonrpc.req.RqFreeSpace;
import cordelia.jsonrpc.res.RsFreeSpace;
import net.miginfocom.swing.MigLayout;
import sb.bdev.text.HumanSize;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class FrmAdd extends JDialog {

    private final AtomicReference<TrClient> client;
    private final JLabel filesLabel = new JLabel("Files not selected");
    private final JLabel destinationLabel = new JLabel();
    private final JTextField urlField = new JTextField();
    private final JTextField destinationField = new JTextField();

    private final List<File> files = new ArrayList<>();

    public FrmAdd(Frame owner, AtomicReference<TrClient> client) {
        super(owner, TransmissionRemote.APP_NAME, true);
        this.client = client;

        setLayout(new MigLayout("fillX, insets 3, wrap", "", "[]push[]"));

        JPanel fields = new JPanel(new MigLayout("fillX, wrap 3", "[right][fill, grow][]"));
        fields.add(new JLabel("Open torrent file(s)"), "span 2, left");
        fields.add(new JButton(new OpenAction()));

        fields.add(filesLabel, "span 3, left");
        filesLabel.setForeground(Color.DARK_GRAY);
        filesLabel.setVisible(false);

        fields.add(new JLabel("or enter an URL"));
        fields.add(urlField, "span 2");

        fields.add(new JLabel("Destination"));
        fields.add(destinationField, "span 2");

        fields.add(destinationLabel, "span 3, right");
        destinationLabel.setForeground(Color.GRAY);

        add(fields, "grow");

        JPanel cmdPanel = new JPanel(new MigLayout("insets 5, fillX, noGrid"));
        cmdPanel.add(new JButton(new OkAction()));
        cmdPanel.add(new JButton(new CancelAction()));
        add(cmdPanel, "span, center");

        setPreferredSize(new Dimension(400, 250));
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
        SwingUtilities.invokeLater(this::updateFields);
    }

    private void updateFields() {
        destinationField.requestFocus();
        destinationField.setText(AppProps.get(AppProps.LAST_DESTINATION));
        destinationLabel.setText(printFS());
        destinationField.getDocument().addDocumentListener(new AbstractDocListener() {
            @Override
            public void update(DocumentEvent e) {
                destinationLabel.setText(printFS());
            }
        });
    }

    private String printFS() {
        RqFreeSpace.Params params = RqFreeSpace.Params.builder()
                .path(destinationField.getText())
                .build();
        RsFreeSpace res = client.get().execute(new RqFreeSpace(TransmissionRemote.TAG, params));
        RsFreeSpace.Result result = res.getResult();
        return result == null
                ? "no such directory"
                : String.format("%s free", new HumanSize(result.getSizeBytes(), HumanSize.US, 2));
    }

    private static final FilenameFilter TORRENT_FILTER = (dir, name) -> name.toLowerCase().endsWith(".torrent");

    private final class OkAction extends AbstractAction {

        public OkAction() {
            super("OK");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!destinationField.getText().isEmpty()) {
                AppProps.putVal(AppProps.LAST_DESTINATION, destinationField.getText());
                SwingUtilities.invokeLater(() -> {
                    TrSource source = null;
                    if (!files.isEmpty())
                        source = new TrSourceFile(files, destinationField.getText());
                    if (!urlField.getText().isEmpty())
                        source = new TrSourceUrl(urlField.getText(), destinationField.getText());
                    try {
                        if (source != null)
                            new TrSourceTrash(source).add(client.get());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(FrmAdd.this, ex.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
                    }
                });
                dispose();
            }
        }
    }

    private final class CancelAction extends AbstractAction {

        public CancelAction() {
            super("Cancel");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    private final class OpenAction extends AbstractAction {

        private static final int MAX_LENGTH = 50;

        public OpenAction() {
            super("", TransmissionRemote.ICON_FOLDER_OPEN);
        }

        private String fitToWidth(String source) {
            if (source.length() > MAX_LENGTH)
                return source.substring(0, MAX_LENGTH - 3) + "...";
            return source;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            files.clear();
            String lastPath = AppProps.get(AppProps.LAST_OPEN_PATH, System.getProperty("user.home"));
            File destination = new File(lastPath).exists() ?
                    new File(lastPath) :
                    new File(System.getProperty("user.home"));
            FileDialog chooser = new FileDialog(FrmAdd.this, "Open torrent file(s)", FileDialog.LOAD);
            chooser.setDirectory(destination.getAbsolutePath());
            chooser.setMultipleMode(true);
            chooser.setFile("*.torrent");
            chooser.setFilenameFilter(TORRENT_FILTER);
            chooser.setVisible(true);
            for (File file : chooser.getFiles()) {
                files.add(file);
                filesLabel.setVisible(true);
                filesLabel.setText(files.size() == 1 ?
                        fitToWidth(files.getFirst().getName()) :
                        String.format("selected %d files", files.size()));
                AppProps.putVal(AppProps.LAST_OPEN_PATH, files.getFirst().getParent());
            }
        }
    }
}
