package WorldApp.GUI;

import Util.Util;
import WorldApp.CORE.Map;
import WorldApp.WorldApp;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ControlPanel extends JPanel {
    private Map map;

    public ControlPanel(Map map) {
        super();
        this.map = map;
        setSize(800, 100);
        setLayout(new GridLayout(2, 0));
        JLabel artifactCategoryLabel = new JLabel("Artifact Category: ");
        JLabel artifactCategory = new JLabel(map.artifactToPlace.toString());
        JButton artifactCategoryChangeButton = new JButton("Change");
        JButton randomArtifactButton = new JButton("Random");
        randomArtifactButton.addActionListener(e -> {
            for (int i = 0; i < 200; i++) {
                Category category = Category.values()[(int) (Math.random() * Category.values().length)];
                int sectorX = (int) Math.floor(Math.random() * WorldApp.sectors);
                int sectorY = (int) Math.floor(Math.random() * WorldApp.sectors);
                int fieldX = (int) Math.floor(Math.random() * WorldApp.fieldsPerSector);
                int fieldY = (int) Math.floor(Math.random() * WorldApp.fieldsPerSector);
                Artifact a = switch (category) {
                    case EMPTY -> new Blank(map.artifactExcavationTime);
                    case GOLD -> new Treasure(map.artifactExcavationTime, Category.GOLD);
                    case SILVER -> new Treasure(map.artifactExcavationTime, Category.SILVER);
                    case BRONZE -> new Treasure(map.artifactExcavationTime, Category.BRONZE);
                    case IRON -> new Treasure(map.artifactExcavationTime, Category.IRON);
                    case OTHER -> new Junk(map.artifactExcavationTime);
                };
                map.placeArtifact(sectorX, sectorY, fieldX, fieldY, a);
            }

        });
        artifactCategoryChangeButton.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("Change Artifact Category");
            dialog.setSize(300, 200);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            JButton emptyButton = new JButton("Empty");
            emptyButton.addActionListener(e1 -> {
                map.artifactToPlace = Category.EMPTY;
                artifactCategory.setText(map.artifactToPlace.toString());
                dialog.dispose();
            });
            JButton junkButton = new JButton("Junk");
            junkButton.addActionListener(e1 -> {
                map.artifactToPlace = Category.OTHER;
                artifactCategory.setText(map.artifactToPlace.toString());
                dialog.dispose();
            });
            JButton goldButton = new JButton("Gold");
            goldButton.addActionListener(e1 -> {
                map.artifactToPlace = Category.GOLD;
                artifactCategory.setText(map.artifactToPlace.toString());
                dialog.dispose();
            });
            JButton silverButton = new JButton("Silver");
            silverButton.addActionListener(e1 -> {
                map.artifactToPlace = Category.SILVER;
                artifactCategory.setText(map.artifactToPlace.toString());
                dialog.dispose();
            });
            JButton bronzeButton = new JButton("Bronze");
            bronzeButton.addActionListener(e1 -> {
                map.artifactToPlace = Category.BRONZE;
                artifactCategory.setText(map.artifactToPlace.toString());
                dialog.dispose();
            });

            dialog.add(emptyButton);
            dialog.add(junkButton);
            dialog.add(goldButton);
            dialog.add(silverButton);
            dialog.add(bronzeButton);

            JTextField excavationTimeField = new JTextField(6);
            excavationTimeField.setText(String.valueOf(map.artifactExcavationTime));
            excavationTimeField.addActionListener(e1 -> {
                try {
                    map.artifactExcavationTime = Integer.parseInt(excavationTimeField.getText());
                } catch (NumberFormatException ignored) {
                }
            });
            dialog.add(excavationTimeField);
            dialog.setLayout(new GridLayout(6, 1));
            dialog.setVisible(true);
        });
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        add(artifactCategoryLabel, c);
        c.gridx = 1;
        add(artifactCategory, c);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 1;
        add(artifactCategoryChangeButton, c);
        c.gridx = 1;
        add(randomArtifactButton, c);
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> artifactCategory.setText(map.artifactToPlace.toString()), 0, 100, TimeUnit.MILLISECONDS);
    }

}
