package SeekerApp;

import SeekerApp.GUI.SeekerFrame;
import SeekerApp.RMI.ISeekerHandler;
import Util.RmiManager;
import interfaces.IClub;
import interfaces.IOffice;
import interfaces.ISeeker;
import interfaces.IWorld;
import lombok.Data;
import model.Report;

import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

@Data
public class SeekerApp {
    public String name;
    public IWorld iw;
    public IOffice io;
    public IClub ic;
    public ISeeker is;
    public boolean worldConnected = false;
    public boolean officeConnected = false;
    public boolean isRegistered = false;
    public RmiManager rmiManager;
    public SeekerFrame sf;

    public ArrayList<Report> reports = new ArrayList<>();

    public static void main(String... args) throws Exception {
        SeekerApp instance = new SeekerApp();
        instance.rmiManager = new RmiManager();
        instance.sf = new SeekerFrame(instance);
        instance.is = new ISeekerHandler(instance);
        /*
      
         *
         * */
    }


    public boolean connectIWorld(String host, int port) {
        System.out.println("Connecting to world...");
        try {
            iw = (IWorld) rmiManager.getRemoteInterface(host, port, "IWorld");
        } catch (Exception e) {
            worldConnected = false;
            System.out.println("Failed to connect to world");
            return false;
        }
        worldConnected = true;
        System.out.println("Connected to world");
        return true;
    }

    public boolean connectIOffice(String host, int port) {
        System.out.println("Connecting to office...");
        try {
            io = (IOffice) LocateRegistry.getRegistry(host, port).lookup("IOffice");
        } catch (Exception e) {
            officeConnected = false;
            e.printStackTrace();
            System.out.println("Failed to connect to office");
            return false;
        }
        officeConnected = true;
        System.out.println("Connected to office");
        return true;
    }

    public void registerAtClub(IClub selectedClub, String seekerName) {
        try {
            name = seekerName;
            System.out.println("Registering as \" " + seekerName + "\" at club " + selectedClub.getName());
            selectedClub.register(is);
            ic = selectedClub;
        } catch (Exception e) {
            isRegistered = false;
            return;
        }
        isRegistered = true;
    }

    public void unregisterFromClub() {
        try {
            ic.unregister(name);
        } catch (Exception e) {
            e.printStackTrace();
            isRegistered = true;
            return;
        }
        isRegistered = false;
    }

    public boolean report(Report r) {
        try {
            ic.report(r, name);
            reports.add(r);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
