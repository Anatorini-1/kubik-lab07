package OfficeApp.CORE;

import interfaces.IClub;
import lombok.SneakyThrows;
import model.Report;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ClubRegistry {
    private HashMap<String, ClubModel> clubs;
    public PermissionRegistry permissionRegistry;

    public ClubRegistry() {
        clubs = new HashMap<>();
    }

    public void addClub(String name, ClubModel club) {
        clubs.put(name, club);
    }

    @SneakyThrows
    public boolean addClub(IClub club) {
        ClubModel clubModel = new ClubModel();
        clubModel.setClub(club);
        clubModel.setName(club.getName());
        clubModel.setReports(new ArrayList<>());
        clubModel.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        if (clubs.containsKey(club.getName())) return false;
        clubs.put(club.getName(), clubModel);
        return true;
    }

    public ClubModel getClub(String name) {
        return clubs.get(name);
    }

    @SneakyThrows
    public ClubModel getClub(IClub club) {
        return clubs.get(club.getName());
    }

    public void removeClub(String name) {
        clubs.remove(name);
    }

    public ArrayList<IClub> getClubs() {
        ArrayList<IClub> clubs = new ArrayList<>();
        clubs = this.clubs.keySet().stream().map(key -> this.clubs.get(key).getClub()).collect(Collectors.toCollection(ArrayList::new));
        return clubs;
    }

    public ArrayList<ClubModel> getClubModels() {
        return new ArrayList<>(clubs.values());
    }

    public boolean clubReport(List<Report> reportList, String clubName) {
        ClubModel club = clubs.get(clubName);
        if (club == null) {
            return false;
        }
        club.getReports().addAll(reportList);
        return true;
    }

    public void setPermissionRegistry(PermissionRegistry permissionRegistry) {
        this.permissionRegistry = permissionRegistry;
    }
}
