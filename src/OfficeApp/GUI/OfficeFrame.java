package OfficeApp.GUI;

import OfficeApp.CORE.ArtifactRegistry;
import OfficeApp.CORE.ClubRegistry;
import OfficeApp.CORE.PermissionRegistry;
import lombok.Data;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OfficeFrame extends JFrame {
    @Getter
    private ArtifactRegistry ar;
    @Getter
    private ClubRegistry cr;
    @Getter
    private OfficeMapPanel omp;
    @Getter
    private OfficeRegisteredClubsPanel orcp;

    public OfficeFrame(ArtifactRegistry ar, ClubRegistry cr, PermissionRegistry pr) {
        super("Office");
        this.ar = ar;
        this.cr = cr;
        this.omp = new OfficeMapPanel(ar, pr);
        this.orcp = new OfficeRegisteredClubsPanel(cr);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.8;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        add(omp, c);
        c.gridx = 1;
        c.weightx = 0.2;
        add(orcp, c);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            omp.repaint();
        }, 0, 1, TimeUnit.SECONDS);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setVisible(true);

    }
}

