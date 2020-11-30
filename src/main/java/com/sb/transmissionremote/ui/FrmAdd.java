package com.sb.transmissionremote.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileFilter;

import com.sb.transmissionremote.AppProperties;
import com.sb.transmissionremote.TransmissionRemote;
import com.sb.transmissionremote.model.*;
import com.sb.transmissionremote.util.AbstractDocListener;
import cordelia.client.Client;
import cordelia.client.TrResponse;
import cordelia.rpc.FreeSpace;
import net.miginfocom.swing.MigLayout;

public final class FrmAdd extends JDialog {

    private final AtomicReference<Client> client;
    private final JLabel filesLabel = new JLabel("Files not selected");
    private final JLabel destinationLabel = new JLabel();
    private final JTextField urlField = new JTextField();
    private final JTextField destinationField = new JTextField();

    private final List<File> files = new ArrayList<>();

    public FrmAdd(Frame owner, AtomicReference<Client> client) {
        super(owner, TransmissionRemote.APP_NAME, true);
        this.client = client;

        setLayout(new MigLayout("fillx, insets 10, wrap 3", "[right][fill, grow][]", "[][][][]push[]"));

        add(new JLabel("Select torrent file(s)"), "span 2, left");
        add(new JButton(new OpenAction()), "right");
        add(filesLabel, "span 3, left");
        filesLabel.setForeground(Color.DARK_GRAY);
        filesLabel.setVisible(false);

        add(new JLabel("Or enter an URL"));
        add(urlField, "span 2");

        add(new JLabel("Destination"));
        add(destinationField);
        destinationLabel.setForeground(Color.GRAY);
        add(destinationLabel);

        JPanel cmdPanel = new JPanel(new MigLayout("insets 5, fillx, nogrid"));
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
        destinationField.setText(AppProperties.get().lastDestination());
        destinationLabel.setText(printFS());
        destinationField.getDocument().addDocumentListener(new AbstractDocListener() {
            @Override
            public void update(DocumentEvent e) {
                destinationLabel.setText(printFS());
            }
        });
    }

    private String printFS() {
        Double bytes = (Double) client.get()
                .post(new FreeSpace(destinationField.getText()), TrResponse.class)
                .get("size-bytes");
        return bytes == null || bytes < 0 ?
                "no such directory" :
                String.format("%s free", new HumanSize(bytes));
    }

    private static final FileFilter TORRENT_FILTER = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".torrent");
        }

        @Override
        public String getDescription() {
            return "Torrent Files (*.torrent)";
        }
    };

    private final class OkAction extends AbstractAction {

        public OkAction() {
            super("OK");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!destinationField.getText().isEmpty()) {
                AppProperties.get().setLastDestination(destinationField.getText());
                Map<String, Object> args = Map.of("download-dir", destinationField.getText());
                SwingUtilities.invokeLater(() -> {
                    TrSource source = null;
                    if (!files.isEmpty())
                        source = new TrSourceFile(files, args);
                    if (!urlField.getText().isEmpty())
                        source = new TrSourceUrl(urlField.getText(), args);
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

        public OpenAction() {
            super("", TransmissionRemote.ICON_FOLDER_OPEN);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            files.clear();
            File dest = !new File(AppProperties.get().lastOpenPath()).exists() ?
                    new File(System.getProperty("user.home")) :
                    new File(AppProperties.get().lastOpenPath());
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Open a torrent file");
            chooser.setCurrentDirectory(dest);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(TORRENT_FILTER);
            chooser.setMultiSelectionEnabled(true);
            int res = chooser.showOpenDialog(FrmAdd.this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File[] selected = chooser.getSelectedFiles();
                if (selected.length > 0) {
                    Collections.addAll(files, selected);
                    filesLabel.setVisible(true);
                    filesLabel.setText(files.size() == 1 ?
                            files.get(0).getName() :
                            String.format("selected %d files", files.size()));
                    AppProperties.get().setLastOpenPath(files.get(0).getParent());
                }
            }
        }
    }
}
