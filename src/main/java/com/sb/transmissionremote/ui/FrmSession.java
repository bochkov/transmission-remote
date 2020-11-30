package com.sb.transmissionremote.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.sb.transmissionremote.AppProperties;
import com.sb.transmissionremote.TransmissionRemote;
import com.sb.transmissionremote.util.Callback;
import net.miginfocom.swing.MigLayout;

public final class FrmSession extends JDialog {

    private final transient AppProperties props = AppProperties.get();
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
        serverField.setText(props.url());
        add(new JLabel());
        add(authField, "shrink");
        add(new JLabel("Username"));
        add(userField);
        userField.setText(props.username());
        userField.setEnabled(!props.username().isEmpty());
        add(new JLabel("Password"));
        add(passField);
        passField.setText(props.password());
        passField.setEnabled(!props.password().isEmpty());
        authField.setSelected(userField.isEnabled() || passField.isEnabled());
        authField.addActionListener(e -> {
            JCheckBox src = (JCheckBox) e.getSource();
            userField.setEnabled(src.isSelected());
            passField.setEnabled(src.isSelected());
        });

        JPanel cmdPanel = new JPanel(new MigLayout());
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
            if (authField.isSelected())
                props.setUrl(serverField.getText(), userField.getText(), passField.getText());
            else
                props.setUrl(serverField.getText(), "", "");
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
