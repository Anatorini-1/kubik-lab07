package ClubApp.RMI;

import ClubApp.ClubApp;
import interfaces.IClub;
import interfaces.ISeeker;
import model.Report;

import java.io.Serializable;
import java.rmi.RemoteException;

public class IClubHandler implements IClub, Serializable {
    private ClubApp app;

    public IClubHandler(ClubApp app) {
        this.app = app;
    }

    @Override
    public boolean register(ISeeker ic) throws RemoteException {
        System.out.println("Registering seeker " + "'" + ic.getName() + "'");
        return app.seekerRegistry.registerSeeker(ic);
    }

    @Override
    public boolean unregister(String seekerName) throws RemoteException {
        return app.seekerRegistry.unregisterSeeker(seekerName);
    }

    @Override
    public String getName() throws RemoteException {
        return app.clubName;
    }

    @Override
    public boolean report(Report report, String seekerName) throws RemoteException {
        return app.seekerRegistry.report(report, seekerName);
    }
}
