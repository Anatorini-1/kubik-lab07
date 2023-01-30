package OfficeApp.GUI;

import OfficeApp.CORE.ClubModel;
import OfficeApp.CORE.ClubRegistry;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class OfficeRegisteredClubsPanel extends JPanel {
    private ClubRegistry cr;

    private HashMap<String, ClubPanel> clubModelArrayList;

    public OfficeRegisteredClubsPanel(ClubRegistry cr) {
        this.cr = cr;
        this.clubModelArrayList = new HashMap<>();
        setLayout(new GridLayout(0, 1));
        setSize(200, 800);
        cr.getClubModels().stream().forEach(cm -> {
            ClubPanel cp = new ClubPanel(cm);
            OfficeRegisteredClubsPanel.this.add(cp);
            this.clubModelArrayList.put(cm.getName(), cp);
        });

    }

    public void addClub(ClubModel clubModel) {
        //System.out.println("Adding" + clubModel.getName());
        ClubPanel cp = new ClubPanel(clubModel);
        this.add(cp);
        this.clubModelArrayList.put(clubModel.getName(), cp);
        //System.out.println(clubModelArrayList);
        repaint();
        revalidate();
    }

    public void removeClub(String name) {
        this.remove(clubModelArrayList.get(name));
        clubModelArrayList.remove(name);
        repaint();
        revalidate();
    }

    private class ClubPanel extends JPanel {

        public ClubPanel(ClubModel cm) {
            JButton detailsButton = new JButton(cm.getName());
            detailsButton.setSize(200, 50);
            detailsButton.setBackground(cm.getColor());
            detailsButton.addActionListener(e -> {
                JDialog dialog = new JDialog();
                dialog.setSize(400, 400);
                dialog.setLayout(new GridLayout(0, 2));
                dialog.add(new JLabel("Name:"));
                dialog.add(new JLabel(cm.getName()));
                dialog.add(new JLabel("Assigned sectors :"));
                String s = cr.permissionRegistry.getClubPermissions(cm.getName()).stream().reduce((s1, s2) -> s1 + ", " + s2).orElse("None").toString();
                dialog.add(new JLabel(s));
                dialog.add(new JLabel("Reports:"));
                dialog.add(new JLabel(cm.getReports().size() + ""));
                cm.getReports().forEach(r -> {
                    dialog.add(new JLabel(r.getSector() + ":" + r.getField()));
                    dialog.add(new JLabel(r.getArtifact().getCategory() + ""));
                });
                dialog.setVisible(true);
            });
            add(detailsButton);
        }
    }
}
