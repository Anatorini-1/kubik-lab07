package WorldApp.GUI;

import WorldApp.CORE.Map;

import javax.swing.*;
import java.awt.*;

public class WorldFrame extends JFrame {
    private Map map;
    private MapPanel mapPanel;
    private ControlPanel controlPanel;

    public WorldFrame(Map map) {
        super("WorldApp");
        this.map = map;
        this.mapPanel = new MapPanel(map);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.controlPanel = new ControlPanel(map);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 900);


        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 8f / 9f;
        getContentPane().add(mapPanel, c);
        c.gridy = 1;
        c.weighty = 1f / 9f;
        getContentPane().add(controlPanel, c);
        getContentPane().add(new JLabel("YOOOOOOO"), c);

        setVisible(true);
    }

}
