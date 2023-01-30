package SeekerApp.GUI;

import SeekerApp.SeekerApp;

import javax.swing.*;
import java.awt.*;

public class SeekerConfigPanel extends JPanel {
    SeekerConfigPanel(SeekerApp app) {
        setSize(150, 150);
        setLayout(new GridLayout(2, 0));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        setBackground(Color.ORANGE);
        add(new SeekerWorldConfig(app), c);
        c.gridy = 1;
        add(new SeekerClubConfig(app), c);
    }
}
