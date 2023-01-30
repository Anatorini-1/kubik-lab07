package WorldApp.RMI;

import WorldApp.CORE.Map;
import Util.Util;
import WorldApp.WorldApp;
import interfaces.IWorld;
import model.Artifact;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class IWorldHandler extends UnicastRemoteObject implements IWorld {
    private final Map map;

    @Override
    public Artifact explore(String seekerName, String sector, String field) throws RemoteException {
        int[] sectorCoords = Util.labelToCoords(sector, WorldApp.sectors, WorldApp.sectors);
        int[] fieldCoords = Util.labelToCoords(field, WorldApp.fieldsPerSector, WorldApp.fieldsPerSector);
        //System.out.println("IWorldHandler.explore() called. Args: " + seekerName + ", " + sector + ", " + field);
        return map.explore(sectorCoords[0], sectorCoords[1], fieldCoords[0], fieldCoords[1]);
    }

    public IWorldHandler(Map map) throws RemoteException {
        super();
        this.map = map;
    }
}
