package ClubApp.GUI;

import ClubApp.ClubApp;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ClubConfigPanel extends JPanel {
    private JButton registerButton;
    private JButton unregisterButton;
    private JTextField nameField;
    private JTextField officeHost;
    private JTextField officePort;
    private ClubApp app;

    ClubConfigPanel(ClubApp app) {
        this.app = app;
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            app.clubName = nameField.getText();
            app.isConnected = app.connectOffice(officeHost.getText(), Integer.parseInt(officePort.getText()));
            if (app.isConnected) {
                //System.out.println(app.clubName);
                registerButton.setEnabled(false);
                registerButton.setText("REGISTERED");
                unregisterButton.setEnabled(true);
                // registerButton.setBackground(Color.GREEN);
                //registerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));
                // registerButton.setForeground(Color.BLACK);
                officeHost.setEnabled(false);
                officePort.setEnabled(false);
                nameField.setEnabled(false);
            }
        });
        unregisterButton = new JButton("Unregister");
        unregisterButton.setEnabled(false);
        unregisterButton.addActionListener(e -> {
            app.isConnected = !app.disconnectOffice();
            if (!app.isConnected) {
                registerButton.setEnabled(true);
                registerButton.setText("Register");
                unregisterButton.setEnabled(false);
                //registerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));
                // registerButton.setForeground(Color.BLACK);
                officeHost.setEnabled(true);
                officePort.setEnabled(true);
                nameField.setEnabled(true);
            }
        });
        nameField = new JTextField("");
        nameField.setHorizontalAlignment(JTextField.CENTER);
        officeHost = new JTextField("127.0.0.1");
        officeHost.setHorizontalAlignment(JTextField.CENTER);
        officePort = new JTextField("1099");
        officePort.setHorizontalAlignment(JTextField.CENTER);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5f;
        c.weighty = 0.25f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5, 5, 5, 5);
        add(new JLabel("Club name:"), c);
        c.gridx = 1;
        add(nameField, c);
        c.gridx = 0;
        c.gridy = 1;
        add(new JLabel("Office IP:"), c);
        c.gridx = 1;
        add(officeHost, c);
        c.gridx = 0;
        c.gridy = 2;
        add(new JLabel("Office port: "), c);
        c.gridx = 1;
        add(officePort, c);
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.NONE;
        add(registerButton, c);
        c.gridy = 4;
        add(unregisterButton, c);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Config", TitledBorder.CENTER, TitledBorder.TOP));
    }
}
