package com.sb.transmissionremote.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.sb.transmissionremote.AppProps;
import com.sb.transmissionremote.TransmissionRemote;
import com.sb.transmissionremote.util.Callback;
import net.miginfocom.swing.MigLayout;

public final class FrmSession extends JDialog {

    private final transient Callback callback;

    private final JTextField serverField = new JTextField();
    private final JCheckBox authField = new JCheckBox("Authentication");
    private final JTextField userField = new JTextField();
    private final JTextField passField = new JPasswordField();

    public FrmSession(Frame owner, Callback callback) {
        super(owner, TransmissionRemote.APP_NAME, true);
        this.callback = callback;
        setLayout(new MigLayout("fillx, wrap 2, insets 10", "[right][fill, grow]"));

        add(new JLabel("URL"));
        add(serverField);
        serverField.setText(AppProps.get(AppProps.TRANSMISSION_URL));
        add(new JLabel());
        add(authField, "shrink");
        add(new JLabel("Username"));
        add(userField);
        String username = AppProps.get(AppProps.TRANSMISSION_USER);
        userField.setText(username);
        userField.setEnabled(username != null && !username.isEmpty());
        add(new JLabel("Password"));
        add(passField);
        String password = AppProps.get(AppProps.TRANSMISSION_PASS);
        passField.setText(password);
        passField.setEnabled(password != null && !password.isEmpty());
        authField.setSelected(userField.isEnabled() || passField.isEnabled());
        authField.addActionListener(e -> {
            JCheckBox src = (JCheckBox) e.getSource();
            userField.setEnabled(src.isSelected());
            passField.setEnabled(src.isSelected());
        });

        var cmdPanel = new JPanel(new MigLayout());
        cmdPanel.add(new JButton(new OkAction()));
        cmdPanel.add(new JButton(new CancelAction()));
        add(cmdPanel, "span 2");

        setMinimumSize(new Dimension(400, 200));
        setResizable(false);
        pack();
        setLocationRelativeTo(owner);
    }

    private final class OkAction extends AbstractAction {

        public OkAction() {
            super("OK");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AppProps.putVal(AppProps.TRANSMISSION_URL, serverField.getText());
            if (authField.isSelected()) {
                AppProps.putVal(AppProps.TRANSMISSION_USER, userField.getText());
                AppProps.putVal(AppProps.TRANSMISSION_PASS, passField.getText());
            }
            FrmSession.this.dispose();
            callback.call();
        }
    }

    private final class CancelAction extends AbstractAction {

        public CancelAction() {
            super("Cancel");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            FrmSession.this.dispose();
        }
    }
}
