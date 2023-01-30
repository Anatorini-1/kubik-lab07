package SeekerApp.RMI;

import SeekerApp.SeekerApp;
import interfaces.ISeeker;
import lombok.SneakyThrows;
import model.Artifact;
import model.Report;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ISeekerHandler extends UnicastRemoteObject implements ISeeker {
    @Override
    public boolean exploreTask(String sector, String field) {
        if (!app.isRegistered) return false;
        if (!app.worldConnected) return false;
        if (!app.officeConnected) return false;
        try {
            System.out.println("Exploring " + sector + " " + field);
            Artifact a = app.iw.explore(app.name, sector, field);
            if (a == null) return true;
            try {
                Thread.sleep(a.getExcavationTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println("Found " + a.getCategory());
            Report r = new Report(sector, field, a);
            return app.report(r);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private SeekerApp app;


    public ISeekerHandler(SeekerApp app) throws RemoteException {
        super();
        this.app = app;
    }

    @Override
    public String getName() throws RemoteException {
        return app.name;
    }
}
