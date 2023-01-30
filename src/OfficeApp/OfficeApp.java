package OfficeApp;

import OfficeApp.CORE.ArtifactRegistry;
import OfficeApp.CORE.ClubRegistry;
import OfficeApp.CORE.PermissionRegistry;
import OfficeApp.GUI.OfficeFrame;
import OfficeApp.RMI.IOfficeHandler;
import Util.RmiManager;
import interfaces.IOffice;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;

public class OfficeApp {
    private static IOfficeHandler officeHandler;
    private static RmiManager rmiManager;
    private static ArtifactRegistry artifactRegistry;
    private static ClubRegistry clubRegistry;
    private static PermissionRegistry permissionRegistry;

    public static int sectors = 8;
    public static int fieldsPerSector = 10;

    public static OfficeFrame of;

    static {
        rmiManager = new RmiManager();
        artifactRegistry = new ArtifactRegistry();
        clubRegistry = new ClubRegistry();
        permissionRegistry = new PermissionRegistry(clubRegistry);
        clubRegistry.setPermissionRegistry(permissionRegistry);
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        of = new OfficeFrame(artifactRegistry, clubRegistry, permissionRegistry);
        try {
            officeHandler = new IOfficeHandler(clubRegistry, permissionRegistry, artifactRegistry, of);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        rmiManager.init(1099);
        rmiManager.bind("IOffice", officeHandler);
    }
}
