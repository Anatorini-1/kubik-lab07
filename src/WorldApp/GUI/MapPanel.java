package WorldApp.GUI;

import WorldApp.CORE.Map;
import Util.Util;
import WorldApp.WorldApp;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MapPanel extends JPanel {
    private final Map map;
    private Point hoveredSector = new Point(-1, -1);
    private Point hoveredField = new Point(-1, -1);
    private Point selectedSector = new Point(-1, -1);

    enum DisplayMode {
        WORLD, SECTOR
    }

    private DisplayMode displayMode = DisplayMode.WORLD;

    public MapPanel(Map map) {
        super();
        this.map = map;
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
                    if (e.getButton() == 1) {
                        Artifact a = switch (map.artifactToPlace) {
                            case GOLD -> new Treasure(map.artifactExcavationTime, Category.GOLD);
                            case SILVER -> new Treasure(map.artifactExcavationTime, Category.SILVER);
                            case BRONZE -> new Treasure(map.artifactExcavationTime, Category.BRONZE);
                            case IRON -> new Treasure(map.artifactExcavationTime, Category.IRON);
                            case OTHER -> new Junk(map.artifactExcavationTime);
                            case EMPTY -> new Blank(map.artifactExcavationTime);
                            default -> null;
                        };
                        try {
                            map.placeArtifact(selectedSector.x, selectedSector.y, hoveredField.x, hoveredField.y, a);
                        } catch (ArrayIndexOutOfBoundsException ignored) {
                        }

                    } else if (e.getButton() == 3) {
                        displayMode = DisplayMode.WORLD;
                        System.out.println("Displaying world");
                    }
                }

            }
        });
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        checkMousePosition(g2d);
        switch (displayMode) {
            case WORLD -> {
                drawBackground(g2d);
                drawGrid(g2d);
                drawTreasures(g2d);
            }
            case SECTOR -> {
                drawSector(g2d);
            }
        }
    }

    private void drawSector(Graphics2D g2d) {
        float dx = (float) getWidth() / WorldApp.fieldsPerSector;
        float dy = (float) (getHeight() - 1) / WorldApp.fieldsPerSector;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        drawBackground:
        for (int i = 0; i < WorldApp.fieldsPerSector; i++) {
            for (int j = 0; j < WorldApp.fieldsPerSector; j++) {
                if (hoveredField != null && i == hoveredField.x && j == hoveredField.y) {
                    g2d.setColor(new Color(17, 89, 176, 176));
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRect((int) (i * dx), (int) (j * dy), (int) dx, (int) dy);
            }
        }
        drawTreasures:
        for (int i = 0; i < WorldApp.fieldsPerSector; i++) {
            for (int j = 0; j < WorldApp.fieldsPerSector; j++) {
                if (map.getField(selectedSector.x, selectedSector.y, i, j) != null) {
                    g2d.setColor(Util.getArtifactColor(map.getField(selectedSector.x, selectedSector.y, i, j)));
                    g2d.fillOval((int) (i * dx), (int) (j * dy), (int) dx, (int) dy);
                }
            }
        }
        g2d.setColor(Color.BLACK);
        drawGrid:
        for (int i = 0; i < WorldApp.fieldsPerSector + 1; i++) {
            for (int j = 0; j < WorldApp.fieldsPerSector + 1; j++) {
                g2d.drawLine((int) (i * dx), 0, (int) (i * dx), getHeight());
                g2d.drawLine(0, (int) (j * dy), getWidth(), (int) (j * dy));
                g2d.setFont(new Font("Arial", Font.PLAIN, 30));
                g2d.drawString(Util.coordsToLabel(i, j, WorldApp.fieldsPerSector, WorldApp.fieldsPerSector), (int) ((i + 0.25) * dx), (int) ((j + 0.60) * dy));
            }
        }
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(0, 0, getWidth() - 3, getHeight() - 3);
        g2d.setPaint(new Color(255, 0, 0, 145));
        Font f = g2d.getFont();
        g2d.setFont(new Font(f.getName(), Font.BOLD, 300));
        g2d.drawString(Util.coordsToLabel(selectedSector.x, selectedSector.y, WorldApp.sectors, WorldApp.sectors), getWidth() * 0.25f, getHeight() * 0.65f);
        g2d.setStroke(new BasicStroke(1));
    }

    private void drawBackground(Graphics2D g2d) {
        int dx = getWidth() / WorldApp.sectors;
        int dy = getHeight() / WorldApp.sectors;
        for (int i = 0; i < WorldApp.sectors; i++) {
            for (int j = 0; j < WorldApp.sectors; j++) {
                if (hoveredSector.x == i && hoveredSector.y == j) {
                    g2d.setColor(new Color(255, 0, 0, 145));
                } else {
                    g2d.setColor(Color.white);
                }
                g2d.fillRect(i * dx, j * dy, dx, dy);
            }
        }

    }

    private void drawGrid(Graphics2D g2d) {
        float sectorDx = (float) getWidth() / (float) WorldApp.sectors;
        float sectorDy = (float) getHeight() / (float) WorldApp.sectors;
        float fieldDx = sectorDx / (float) WorldApp.fieldsPerSector;
        float fieldDy = sectorDy / (float) WorldApp.fieldsPerSector;

        Font f = g2d.getFont();
        g2d.setFont(new Font(f.getName(), Font.BOLD, 40));

        g2d.setPaint(new Color(255, 0, 0, 145));
        drawLabels:
        for (int i = 0; i < WorldApp.sectors; i++) {
            for (int j = 0; j < WorldApp.sectors; j++) {
                if (i == hoveredSector.x && j == hoveredSector.y) {
                    g2d.setPaint(Color.white);
                } else {
                    g2d.setPaint(new Color(255, 0, 0, 145));
                }
                g2d.drawString(Util.coordsToLabel(i, j, WorldApp.sectors, WorldApp.sectors), (int) ((i + 0.25f) * sectorDx), (int) ((j + 0.6f) * sectorDy));
            }
        }
        g2d.setFont(f);
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1));
        drawFields:
        for (int x = 0; x < WorldApp.sectors; x++) {
            for (int y = 0; y < WorldApp.sectors; y++) {
                for (int innerX = 0; innerX < WorldApp.fieldsPerSector; innerX++) {
                    for (int innerY = 0; innerY < WorldApp.fieldsPerSector; innerY++) {
                        g2d.drawRect((int) (innerX * fieldDx), (int) (innerY * fieldDy), (int) fieldDx, (int) fieldDy);
                    }
                }
                g2d.translate(0, sectorDy);
            }
            g2d.translate(sectorDx, -WorldApp.sectors * sectorDy);
        }
        g2d.translate(-WorldApp.sectors * sectorDx, 0);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(4));
        drawSectors:
        for (int i = 0; i < WorldApp.sectors; i++) {
            g2d.drawLine((int) (sectorDx * i), 0, (int) (sectorDx * i), getHeight());
            g2d.drawLine(0, (int) (sectorDy * i), getWidth(), (int) (i * sectorDy));
        }


    }

    private void drawTreasures(Graphics2D g2d) {
        float sectorDx = (float) getWidth() / (float) WorldApp.sectors;
        float sectorDy = (float) getHeight() / (float) WorldApp.sectors;
        float fieldDx = sectorDx / (float) WorldApp.fieldsPerSector;
        float fieldDy = sectorDy / (float) WorldApp.fieldsPerSector;
        g2d.setColor(Color.RED);
        for (int sectorX = 0; sectorX < WorldApp.sectors; sectorX++) {
            for (int sectorY = 0; sectorY < WorldApp.sectors; sectorY++) {
                for (int fieldX = 0; fieldX < WorldApp.fieldsPerSector; fieldX++) {
                    for (int fieldY = 0; fieldY < WorldApp.fieldsPerSector; fieldY++) {
                        if (map.getField(sectorX, sectorY, fieldX, fieldY) != null) {
                            AffineTransform at = new AffineTransform();
                            AffineTransform old = g2d.getTransform();
                            g2d.setTransform(at);
                            g2d.translate(sectorX * sectorDx + fieldX * fieldDx, sectorY * sectorDy + fieldY * fieldDy);
                            g2d.setColor(Util.getArtifactColor(map.getField(sectorX, sectorY, fieldX, fieldY)));
                            g2d.fillOval(0, 0, (int) fieldDx, (int) fieldDy);
                            g2d.setColor(Color.BLACK);
                            g2d.setTransform(old);
                        }
                    }
                }
            }
        }
    }

    private void checkMousePosition(Graphics2D g2d) {
        int dx = getWidth() / WorldApp.sectors;
        int dy = getHeight() / WorldApp.sectors;
        Point mouse = this.getMousePosition();
        if (mouse == null) {
            hoveredSector = new Point(-1, -1);
            hoveredField = null;
        } else {
            hoveredSector = new Point(mouse.x / dx, mouse.y / dy);
            if (displayMode == DisplayMode.SECTOR) {
                hoveredField = new Point(mouse.x / (getWidth() / WorldApp.fieldsPerSector), mouse.y / (getHeight() / WorldApp.fieldsPerSector));
            } else {
                hoveredField = null;
            }
        }
    }
}
