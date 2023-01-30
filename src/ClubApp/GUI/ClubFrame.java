package ClubApp.GUI;

import ClubApp.ClubApp;

import javax.swing.*;
import java.awt.*;

public class ClubFrame extends JFrame {
    private ClubExcavationPanel excavation;
    private ClubSeekersPanel seekers;
    private ClubConfigPanel config;

    private ClubWorldViewPanel worldView;
    private ClubApp app;

    public ClubFrame(ClubApp app) {
        super("Club");
        this.app = app;
        setSize(900, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        excavation = new ClubExcavationPanel(app);
        seekers = new ClubSeekersPanel(app);
        config = new ClubConfigPanel(app);
        worldView = new ClubWorldViewPanel(app);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 600f / 900f;
        c.weighty = 1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        add(excavation, c);
        c.weightx = 200f / 900f;
        c.gridheight = 1;
        c.weighty = 0.5;
        c.gridx = 1;
        add(worldView, c);
        c.gridy = 1;
        add(config, c);
        c.gridx = 2;
        c.weightx = 100f / 900f;
        c.gridy = 0;
        c.gridheight = 2;
        c.weighty = 1;
        add(seekers, c);
        setVisible(true);
        setVisible(true);
    }
}
