package SeekerApp.GUI;

import SeekerApp.SeekerApp;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SeekerWorldConfig extends JPanel {
    private JTextField worldHost;
    private JTextField worldPort;

    private JButton connectWorld;

    public SeekerWorldConfig(SeekerApp app) {
        super();
        worldHost = new JTextField("127.0.0.1");
        worldPort = new JTextField("2137");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.33;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 3, 3, 3);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "World", TitledBorder.CENTER, TitledBorder.TOP));
        add(new JLabel("Host: "), c);
        c.gridx++;
        add(worldHost, c);
        c.gridx--;
        c.gridy++;
        add(new JLabel("Port: "), c);
        c.gridx++;
        add(worldPort, c);
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        connectWorld = new JButton("Connect");
        connectWorld.addActionListener(e -> {
            app.connectIWorld(worldHost.getText(), Integer.parseInt(worldPort.getText()));
        });
        add(connectWorld, c);
    }
}
