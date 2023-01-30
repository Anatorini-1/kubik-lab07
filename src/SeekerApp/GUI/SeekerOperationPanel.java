package SeekerApp.GUI;

import SeekerApp.SeekerApp;
import model.Report;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SeekerOperationPanel extends JScrollPane {
    private SeekerApp app;

    SeekerOperationPanel(SeekerApp app) {
        super();
        this.app = app;
        setPreferredSize(new Dimension(300, 300));
        setBorder(BorderFactory.createTitledBorder("Artifact found"));
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::update, 0, 250, TimeUnit.MILLISECONDS);
    }

    public void update() {
        if (app.reports.size() == 0) {
            setViewportView(new JLabel("No artifacts found"));
            return;
        }
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        setViewportView(panel);
        for (Report r : app.reports) {
            //System.out.println(r.getArtifact().getCategory());
            panel.add(new JLabel(r.getSector() + " " + r.getField() + " " + r.getArtifact().getCategory()));
        }
        this.revalidate();
        this.repaint();
    }
}
