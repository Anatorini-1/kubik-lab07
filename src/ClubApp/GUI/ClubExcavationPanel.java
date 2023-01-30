package ClubApp.GUI;

import ClubApp.ClubApp;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;

public class ClubExcavationPanel extends JPanel {
    private ClubApp app;

    public ClubExcavationPanel(ClubApp app) {
        super();
        this.app = app;

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::repaint, 0, 1000 / 60, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int sectorWidth = (getWidth() / 2) - 20;
        int sectorHeight = getHeight() - 20;
        int horizontalMargin = 20;
        int verticalMargin = 10;
        int dx = sectorWidth / 10;
        int dy = sectorHeight / 10;
        int i = 0;
        for (String sector : app.sectorsToExplore.keySet()) {
            drawBackground(g2d, i * horizontalMargin + (i * sectorWidth), verticalMargin / 2, sectorWidth, sectorHeight, dx, dy, sector);
            drawLabels(g2d, i * horizontalMargin + (i * sectorWidth), verticalMargin / 2, sectorWidth, sectorHeight, dx, dy, sector);
            drawGrid(g2d, i * horizontalMargin + (i * sectorWidth), verticalMargin / 2, sectorWidth, sectorHeight, dx, dy);
            i++;
        }
        g2d.setColor(Color.black);


    }

    private void drawGrid(Graphics2D g2d, int x, int y, int w, int h, int dx, int dy) {
        g2d.setColor(Color.black);
        for (int i = 0; i < w; i += dx) {
            g2d.drawLine(x + i, y, x + i, y + h);
        }
        for (int i = 0; i < h; i += dy) {
            g2d.drawLine(x, y + i, x + w, y + i);
        }
    }

    private void drawLabels(Graphics2D g2d, int x, int y, int w, int h, int dx, int dy, String backLabel) {
        g2d.setColor(new Color(255, 0, 0, 136));
        g2d.setFont(new Font("Arial", Font.PLAIN, 30));
        g2d.drawString(backLabel, (int) (x + w / 2.2), (int) (y + h / 1.8));
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.setColor(new Color(0, 0, 0, 0.5f));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                g2d.drawString(Util.Util.coordsToLabel(i, j, 10), (int) (x + i * dx + 0.15 * dx), (int) (y + j * dy + 0.6 * dy));
            }
        }
    }

    private void drawBackground(Graphics2D g2d, int x, int y, int w, int h, int dx, int dy, String key) {
        g2d.setColor(Color.white);
        g2d.fillRect(x, y, w, h);
        g2d.setColor(Color.GREEN);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i * 10 + j < app.sectorsToExplore.get(key).get()) {
                    g2d.setColor(Color.GREEN);
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRect(x + j * dx, y + i * dy, dx, dy);
            }
        }
        g2d.setColor(Color.black);
    }
}
