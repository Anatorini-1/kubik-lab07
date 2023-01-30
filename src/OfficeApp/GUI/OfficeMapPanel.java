package OfficeApp.GUI;

import OfficeApp.CORE.ArtifactRegistry;
import OfficeApp.CORE.ClubModel;
import OfficeApp.CORE.PermissionRegistry;
import OfficeApp.OfficeApp;
import Util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OfficeMapPanel extends JPanel {
    enum DisplayMode {WORLD, SECTOR}

    ;
    private DisplayMode displayMode = DisplayMode.WORLD;
    private ArtifactRegistry artifactRegistry;
    private Point hoveredSector = new Point(-1, -1);
    private Point hoveredField = new Point(-1, -1);
    private Point selectedSector = new Point(-1, -1);

    private void checkMousePosition(Graphics2D g2d) {
        int dx = getWidth() / OfficeApp.sectors;
        int dy = getHeight() / OfficeApp.sectors;
        Point mouse = this.getMousePosition();
        if (mouse == null) {
            hoveredSector = new Point(-1, -1);
            hoveredField = null;
        } else {
            hoveredSector = new Point(mouse.x / dx, mouse.y / dy);
            if (displayMode == DisplayMode.SECTOR) {
                hoveredField = new Point(mouse.x / (getWidth() / OfficeApp.fieldsPerSector), mouse.y / (getHeight() / OfficeApp.fieldsPerSector));
            } else {
                hoveredField = null;
            }
        }
    }

    private PermissionRegistry permissionRegistry;

    public OfficeMapPanel(ArtifactRegistry ar, PermissionRegistry pr) {
        super();
        artifactRegistry = ar;
        permissionRegistry = pr;
        //setBackground(Color.red);
        setSize(800, 800);
        //add(new JLabel("123"));
        ScheduledExecutorService sce = Executors.newSingleThreadScheduledExecutor();
        sce.scheduleAtFixedRate(this::repaint, 0, 1000 / 60, TimeUnit.MILLISECONDS);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (displayMode == DisplayMode.WORLD) {
                    selectedSector = hoveredSector;
                    displayMode = DisplayMode.SECTOR;
                    System.out.println("Displaying sector " + selectedSector);
                } else {
                    displayMode = DisplayMode.WORLD;
                    System.out.println("Displaying world");
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        checkMousePosition(g2d);
        switch (displayMode) {
            case SECTOR -> {
                g2d.clearRect(0, 0, getWidth(), getHeight());
                drawSector(g2d);
            }
            case WORLD -> {

                g2d.clearRect(0, 0, getWidth(), getHeight());
                drawBackground(g2d);
                drawPermissions(g2d);
                drawGrid(g2d);
            }

        }
    }

    private void drawBackground(Graphics2D g2d) {
        int dx = getWidth() / OfficeApp.sectors;
        int dy = getHeight() / OfficeApp.sectors;
        for (int i = 0; i < OfficeApp.sectors; i++) {
            for (int j = 0; j < OfficeApp.sectors; j++) {
                if (i == hoveredSector.x && j == hoveredSector.y) {
                    g2d.setColor(Color.red);
                } else {
                    g2d.setColor(Color.white);
                }
                g2d.fillRect(i * dx, j * dy, dx, dy);
            }
        }
    }


    private void drawGrid(Graphics2D g2d) {
        float sectorDx = (float) getWidth() / (float) OfficeApp.sectors;
        float sectorDy = (float) getHeight() / (float) OfficeApp.sectors;
        float fieldDx = sectorDx / (float) OfficeApp.fieldsPerSector;
        float fieldDy = sectorDy / (float) OfficeApp.fieldsPerSector;

        Font f = g2d.getFont();
        g2d.setFont(new Font(f.getName(), Font.BOLD, 40));

        g2d.setPaint(new Color(255, 0, 0, 145));
        drawLabels:
        for (int i = 0; i < OfficeApp.sectors; i++) {
            for (int j = 0; j < OfficeApp.sectors; j++) {
                if (i == hoveredSector.x && j == hoveredSector.y) {
                    g2d.setPaint(Color.white);
                } else {
                    g2d.setPaint(new Color(255, 0, 0, 145));
                }
                g2d.drawString(Util.coordsToLabel(i, j, OfficeApp.sectors, OfficeApp.sectors), (int) ((i + 0.25f) * sectorDx), (int) ((j + 0.6f) * sectorDy));
            }
        }
        g2d.setFont(f);
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1));
        drawFields:
        for (int x = 0; x < OfficeApp.sectors; x++) {
            for (int y = 0; y < OfficeApp.sectors; y++) {
                for (int innerX = 0; innerX < OfficeApp.fieldsPerSector; innerX++) {
                    for (int innerY = 0; innerY < OfficeApp.fieldsPerSector; innerY++) {
                        g2d.drawRect((int) (innerX * fieldDx), (int) (innerY * fieldDy), (int) fieldDx, (int) fieldDy);
                    }
                }
                g2d.translate(0, sectorDy);
            }
            g2d.translate(sectorDx, -OfficeApp.sectors * sectorDy);
        }
        g2d.translate(-OfficeApp.sectors * sectorDx, 0);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(4));
        drawSectors:
        for (int i = 0; i < OfficeApp.sectors; i++) {
            g2d.drawLine((int) (sectorDx * i), 0, (int) (sectorDx * i), getHeight());
            g2d.drawLine(0, (int) (sectorDy * i), getWidth(), (int) (i * sectorDy));
        }
    }

    private void drawSector(Graphics2D g2d) {
        float dx = (float) getWidth() / OfficeApp.fieldsPerSector;
        float dy = (float) (getHeight() - 1) / OfficeApp.fieldsPerSector;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        drawBackground:
        for (int i = 0; i < OfficeApp.fieldsPerSector; i++) {
            for (int j = 0; j < OfficeApp.fieldsPerSector; j++) {
                if (hoveredField != null && i == hoveredField.x && j == hoveredField.y) {
                    g2d.setColor(new Color(17, 89, 176, 176));
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRect((int) (i * dx), (int) (j * dy), (int) dx, (int) dy);
            }
        }
        drawTreasures:
        for (int i = 0; i < OfficeApp.fieldsPerSector; i++) {
            for (int j = 0; j < OfficeApp.fieldsPerSector; j++) {
                if (artifactRegistry.getArtifact(selectedSector.x, selectedSector.y, i, j) != null) {
                    g2d.setColor(Util.getArtifactColor(artifactRegistry.getArtifact(selectedSector.x, selectedSector.y, i, j).getArtifact()));
                    g2d.fillOval((int) (i * dx), (int) (j * dy), (int) dx, (int) dy);
                }
            }
        }
        g2d.setColor(Color.BLACK);
        drawGrid:
        for (int i = 0; i < OfficeApp.fieldsPerSector + 1; i++) {
            for (int j = 0; j < OfficeApp.fieldsPerSector + 1; j++) {
                g2d.drawLine((int) (i * dx), 0, (int) (i * dx), getHeight());
                g2d.drawLine(0, (int) (j * dy), getWidth(), (int) (j * dy));
                g2d.setFont(new Font("Arial", Font.PLAIN, 30));
                g2d.drawString(Util.coordsToLabel(i, j, OfficeApp.fieldsPerSector, OfficeApp.fieldsPerSector), (int) ((i + 0.25) * dx), (int) ((j + 0.60) * dy));
            }
        }
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(0, 0, getWidth() - 3, getHeight() - 3);
        g2d.setPaint(new Color(255, 0, 0, 145));
        Font f = g2d.getFont();
        g2d.setFont(new Font(f.getName(), Font.BOLD, 300));
        g2d.drawString(Util.coordsToLabel(selectedSector.x, selectedSector.y, OfficeApp.sectors, OfficeApp.sectors), getWidth() * 0.25f, getHeight() * 0.65f);
        g2d.setStroke(new BasicStroke(1));
    }

    private void drawPermissions(Graphics2D g2d) {
        int dx = getWidth() / OfficeApp.sectors;
        int dy = getHeight() / OfficeApp.sectors;
        for (int i = 0; i < OfficeApp.sectors; i++) {
            for (int j = 0; j < OfficeApp.sectors; j++) {
                String sector = Util.coordsToLabel(i, j, OfficeApp.sectors, OfficeApp.sectors);
                if (permissionRegistry.checkPermission(sector) != null) {
                    g2d.setColor(permissionRegistry.checkPermission(sector).getColor());
                    g2d.fillRect(i * dx, j * dy, dx, dy);
                }
            }
        }
    }
}
