package ClubApp.GUI;

import ClubApp.ClubApp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClubSeekersPanel extends JPanel {
    private ArrayList<String> seekers;

    ClubSeekersPanel(ClubApp app) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Seekers"));
        seekers = app.seekerRegistry.getSeekerNames();
        for (String name : seekers) {
            JLabel l = new JLabel(name);
            l.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            add(l);
        }
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            ArrayList<String> newSeekers = app.seekerRegistry.getSeekerNames();
            //System.out.println(newSeekers);

            if (newSeekers.size() != seekers.size()) {
                seekers = newSeekers;
                removeAll();
                for (String name : seekers) {
                    JLabel l = new JLabel(name);
                    l.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                    l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    add(l);
                }
                revalidate();
                repaint();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
