package ClubApp.CORE;

import interfaces.ISeeker;
import model.Category;
import model.Report;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class SeekerRegistry implements Serializable {
    private HashMap<String, ClubSeekerModel> seekers;

    public SeekerRegistry() {
        this.seekers = new HashMap<>();
    }

    private ArrayList<Report> newReports = new ArrayList<>();

    public ArrayList<String> getSeekerNames() {
        // System.out.println(this.seekers.keySet().size());
        return new ArrayList<>(seekers.keySet());
    }

    public boolean registerSeeker(ISeeker iSeeker) throws RemoteException {
        String name = iSeeker.getName();
        System.out.println("Registering seeker " + "'" + name + "'");
        if (seekers.containsKey(name)) return false;
        seekers.put(name, new ClubSeekerModel(iSeeker, name));
        System.out.println("Registered seeker: " + name);
        //System.out.println(getSeekerNames());
        return true;
    }

    public boolean report(Report report, String seekerName) {
        if (!seekers.containsKey(seekerName)) return false;
        seekers.get(seekerName).getReportsFiled().add(report);
        if (report.getArtifact().getCategory() != Category.EMPTY && report.getArtifact().getCategory() != Category.OTHER) {
            newReports.add(report);
        }
        System.out.println("Report filed by " + seekerName + ": " + report.getArtifact().getCategory());
        return true;
    }

    public boolean unregisterSeeker(String name) {
        if (seekers.containsKey(name)) {
            seekers.remove(name);
            return true;
        }
        return false;
    }

    public ArrayList<Report> getNewReports() {
        ArrayList<Report> reports = new ArrayList<>(newReports);
        newReports.clear();
        return reports;
    }

    public ClubSeekerModel getSeeker(String name) {
        return seekers.get(name);
    }
}
