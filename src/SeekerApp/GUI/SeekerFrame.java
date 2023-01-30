package SeekerApp.GUI;

import SeekerApp.SeekerApp;

import javax.swing.*;
import java.awt.*;

public class SeekerFrame extends JFrame {
    private SeekerConfigPanel config;
    private SeekerOperationPanel operation;

    public SeekerFrame(SeekerApp app) {
        super("Seeker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 300);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 300f / 450f;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        config = new SeekerConfigPanel(app);
        operation = new SeekerOperationPanel(app);

        add(operation, c);
        c.weightx = 150f / 450f;
        c.gridx = 1;
        add(config, c);
        setVisible(true);
    }
}
