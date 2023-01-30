package OfficeApp.RMI;

import OfficeApp.CORE.ArtifactRegistry;
import OfficeApp.CORE.ClubModel;
import OfficeApp.CORE.ClubRegistry;
import OfficeApp.CORE.PermissionRegistry;
import OfficeApp.GUI.OfficeFrame;
import interfaces.IClub;
import interfaces.IOffice;
import model.Report;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class IOfficeHandler extends UnicastRemoteObject implements IOffice {
    private ClubRegistry cr;
    private PermissionRegistry pr;
    private ArtifactRegistry ar;
    private OfficeFrame of;

    public IOfficeHandler(ClubRegistry cr, PermissionRegistry pr, ArtifactRegistry ar, OfficeFrame of) throws RemoteException {
        this.cr = cr;
        this.pr = pr;
        this.ar = ar;
        this.of = of;
    }

    @Override
    public boolean register(IClub ic) throws RemoteException {
        ClubModel cm = this.cr.getClub(ic);
        if (cm != null) {
            System.out.println("Club " + ic.getName() + " already registered");
            return false;
        }
        System.out.println("Club " + ic.getName() + " registered");
        boolean res = this.cr.addClub(ic);
        if (!res) return false;
        of.getOrcp().addClub(cr.getClub(ic));
        return true;
    }

    @Override
    public boolean unregister(String clubName) throws RemoteException {
        ClubModel cm = this.cr.getClub(clubName);
        System.out.println("Unregistering " + (cm == null ? null : cm.getName()));
        if (cm == null) {
            return false;
        }
        this.cr.removeClub(clubName);
        this.of.getOrcp().removeClub(clubName);
        this.pr.removeClub(clubName);

        return true;
    }

    @Override
    public boolean permissionRequest(String clubName, String sector) throws RemoteException {
        return pr.requestPermission(clubName, sector);
    }

    @Override
    public boolean permissionEnd(String clubName, String sector) throws RemoteException {
        return pr.endPermission(clubName, sector);
    }

    @Override
    public boolean report(List<Report> reports, String clubName) throws RemoteException {
        return cr.clubReport(reports, clubName);
    }

    @Override
    public List<IClub> getClubs() throws RemoteException {
        return cr.getClubs();
    }
}
