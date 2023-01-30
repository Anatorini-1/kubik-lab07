package ClubApp.GUI;

import ClubApp.ClubApp;

import javax.swing.*;
import java.awt.*;

public class ClubWorldViewPanel extends JPanel {
    private JTextField sectorField;
    private JButton exploreButton;

    ClubWorldViewPanel(ClubApp app) {
        
        setLayout(new GridLayout(0, 2));
        add(new JLabel("Sector:"));
        sectorField = new JTextField(5);
        add(sectorField);
        exploreButton = new JButton("Explore");
        exploreButton.addActionListener(e -> {
            boolean res = app.exploreSector(sectorField.getText());
            if (!res) {
                JOptionPane.showMessageDialog(null, "No");
            }
        });
        add(exploreButton);
    }
}
