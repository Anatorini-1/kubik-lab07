package SeekerApp.GUI;

import SeekerApp.SeekerApp;
import interfaces.IClub;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SeekerClubConfig extends JPanel {
    private JTextField officeHost;
    private JTextField officePort;
    private JButton getClubs;
    private JButton connectOffice;
    private JComboBox<String> clubs;
    private JButton connectClub;
    private JButton disconnectClub;
    private IClub selectedClub;
    private JTextField nameField;

    public SeekerClubConfig(SeekerApp app) {
        super();
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Club", TitledBorder.CENTER, TitledBorder.TOP));
        setLayout(new GridLayout(0, 2));
        officeHost = new JTextField("127.0.0.1");
        officePort = new JTextField("1099");
        connectOffice = new JButton("Connect");
        disconnectClub = new JButton("Unregister from club");
        connectClub = new JButton("Register at club");
        nameField = new JTextField();
        getClubs = new JButton("Get Clubs");
        clubs = new JComboBox<>();


        disconnectClub.addActionListener(e -> {
            app.unregisterFromClub();
            if (!app.isRegistered) {
                SeekerClubConfig.this.removeAll();
                SeekerClubConfig.this.setLayout(new GridLayout(0, 2));
                SeekerClubConfig.this.add(getClubs);
                SeekerClubConfig.this.add(connectClub);
                SeekerClubConfig.this.add(nameField);
                SeekerClubConfig.this.add(clubs);
                SeekerClubConfig.this.revalidate();
                SeekerClubConfig.this.repaint();
            }
        });
        connectClub.addActionListener(e -> {
            if (selectedClub != null) {
                app.registerAtClub(selectedClub, nameField.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Please select a club first!");
            }
            if (app.isRegistered) {
                connectOffice.setEnabled(false);
                SeekerClubConfig.this.removeAll();
                SeekerClubConfig.this.setLayout(new GridLayout(0, 1));
                try {
                    SeekerClubConfig.this.add(new JLabel("Registered at club: " + app.ic.getName()));
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                SeekerClubConfig.this.add(disconnectClub);
                SeekerClubConfig.this.revalidate();
                SeekerClubConfig.this.repaint();
            }


        });
        connectOffice.addActionListener(e -> {
            app.connectIOffice(officeHost.getText(), Integer.parseInt(officePort.getText()));
            if (app.officeConnected) {
                setLayout(new GridLayout(0, 2));
                SeekerClubConfig.this.removeAll();
                SeekerClubConfig.this.add(getClubs);
                SeekerClubConfig.this.add(connectClub);
                SeekerClubConfig.this.add(nameField);
                SeekerClubConfig.this.add(clubs);
                SeekerClubConfig.this.revalidate();
                SeekerClubConfig.this.repaint();
            }
        });
        getClubs.addActionListener(e -> {
            try {
                List<IClub> clubsList = app.io.getClubs();
                if (clubsList.size() == 0) {
                    JOptionPane.showMessageDialog(null, "No clubs found");
                    return;
                }
                clubs.removeAllItems();
                for (IClub club : clubsList) {
                    clubs.addItem(club.getName());
                }
                clubs.setSelectedIndex(0);
                selectedClub = clubsList.get(0);
                clubs.addActionListener(e1 -> {
                    selectedClub = clubsList.get(clubs.getSelectedIndex());
                });

            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            } finally {
                SeekerClubConfig.this.revalidate();
                SeekerClubConfig.this.repaint();
            }

        });

        add(new JLabel("Host: "));
        add(officeHost);
        add(new JLabel("Port: "));
        add(officePort);
        add(connectOffice);
    }

}
