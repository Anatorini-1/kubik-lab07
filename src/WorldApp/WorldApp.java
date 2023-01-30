package WorldApp;

import WorldApp.CORE.Map;
import WorldApp.GUI.WorldFrame;
import WorldApp.RMI.IWorldHandler;
import Util.RmiManager;

import java.rmi.RemoteException;

public class WorldApp {
    private static final Map map;
    public static final int sectors;
    public static final int fieldsPerSector;

    static {
        sectors = 8;
        fieldsPerSector = 10;
        map = new Map();
    }

    public static void main(String... args) throws RemoteException {
        WorldFrame wf = new WorldFrame(map);
        RmiManager rmiManager = new RmiManager();
        IWorldHandler worldHandler = new IWorldHandler(map);
        rmiManager.init(1100);
        rmiManager.bind("IWorld", worldHandler);

    }
}
