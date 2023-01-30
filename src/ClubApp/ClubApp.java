package ClubApp;

import ClubApp.CORE.ClubSeekerModel;
import ClubApp.CORE.SeekerRegistry;
import ClubApp.GUI.ClubFrame;
import ClubApp.RMI.IClubHandler;
import OfficeApp.RMI.IOfficeHandler;
import Util.RmiManager;
import interfaces.IClub;
import interfaces.IOffice;
import interfaces.ISeeker;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ClubApp implements Serializable {
    private RmiManager rmiManager;
    public String clubName;
    public SeekerRegistry seekerRegistry;

    public IOffice iOffice;
    private IClub iClub;
    public HashMap<String, AtomicInteger> sectorsToExplore;
    public boolean isConnected = false;

    private ScheduledExecutorService reporter;

    private ScheduledExecutorService explorer;

    public static void main(String... args) {
        ClubApp instance = new ClubApp();
        ClubFrame cf = new ClubFrame(instance);
        
    }

    public ClubApp() {
        rmiManager = new RmiManager();
        seekerRegistry = new SeekerRegistry();
        sectorsToExplore = new HashMap<>();
        explorer = Executors.newSingleThreadScheduledExecutor();
        Set<String> sectorsToRemove = new HashSet<>();
        ArrayList<Future<Boolean>> futures = new ArrayList<>();
        explorer.scheduleAtFixedRate(() -> {
            outer:
            for (String sector : sectorsToExplore.keySet()) {
                if (sectorsToExplore.get(sector).get() == 100) {
                    sectorsToRemove.add(sector);
                    continue;
                }
                for (String seeker : seekerRegistry.getSeekerNames()) {
                    ClubSeekerModel seekerModel = seekerRegistry.getSeeker(seeker);
                    if (sectorsToExplore.get(sector).get() >= 100) {
                        sectorsToRemove.add(sector);
                        continue outer;
                    }
                    Future<Boolean> f = seekerModel.getExecutor().submit(() -> {
                        try {
                            boolean b = seekerModel.getISeeker().exploreTask(sector, Util.Util.coordsToLabel(sectorsToExplore.get(sector).get() % 10, sectorsToExplore.get(sector).getAndIncrement() / 10, 10));
                            return b;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return false;
                    });
                    futures.add(f);
                }
                try {
                    for (Future<Boolean> future : futures) {
                        future.get();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                futures.clear();
            }
            for (String sector : sectorsToRemove) {
                sectorsToExplore.remove(sector);
                try {
                    iOffice.permissionEnd(clubName, sector);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            sectorsToRemove.clear();
            /*
            for (String sector : sectorsToExplore.keySet()) {
                ArrayList<String> seekerNames = seekerRegistry.getSeekerNames();
                if (seekerNames.size() == 0) {
                    System.out.println("No seekers available");
                    continue;
                }
                ClubSeekerModel sm = seekerRegistry.getSeeker(seekerNames.get(0));
                if (sm == null) {
                    System.out.println("No seeker available");
                    return;
                }
                ISeeker seeker = sm.getISeeker();
                try {
                    seeker.exploreTask(sector, Util.Util.coordsToLabel(sectorsToExplore.get(sector) % 10, sectorsToExplore.get(sector) / 10, 10));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                sectorsToExplore.put(sector, sectorsToExplore.get(sector) + 1);
                //System.out.println("Exploring " + sector + " " + sectorsToExplore.get(sector));
                if (sectorsToExplore.get(sector) == 100) {
                    try {
                        boolean res = iOffice.permissionEnd(clubName, sector);
                        if (res) {
                            System.out.println("Permission ended for " + sector);
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    sectorsToRemove.add(sector);
                }
            }
            for (String sector : sectorsToRemove) {
                sectorsToExplore.remove(sector);
            }
            sectorsToRemove.clear();*/
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    @SneakyThrows
    public boolean exploreSector(String sector) {
        if (iOffice == null) return false;
        if (sectorsToExplore.size() >= 2) return false;
        boolean res = iOffice.permissionRequest(clubName, sector);
        if (!res) return false;
        sectorsToExplore.put(sector, new AtomicInteger(0));
        return true;
    }

    public boolean connectOffice(String host, int port) {
        try {
            iClub = new IClubHandler(this);
            System.out.println("Registering as " + iClub.getName());
            iOffice = (IOffice) rmiManager.getRemoteInterface(host, port, "IOffice");
            IClub ic = (IClub) UnicastRemoteObject.exportObject(iClub, 0);
            boolean res = iOffice.register(ic);
            if (res) {
                reporter = Executors.newSingleThreadScheduledExecutor();
                reporter.scheduleAtFixedRate(() -> {
                    try {
                        iOffice.report(ClubApp.this.seekerRegistry.getNewReports(), clubName);
                    } catch (RemoteException r) {
                        r.printStackTrace();
                    }
                }, 0, 1, TimeUnit.SECONDS);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SneakyThrows
    public boolean disconnectOffice() {
        iClub = null;
        try {
            boolean res = iOffice.unregister(clubName);
            //System.out.println("recived " + res);
            reporter.shutdown();
            reporter.awaitTermination(1, TimeUnit.SECONDS);
            reporter = null;
            iOffice = null;
            return res;
        } catch (RemoteException r) {
            r.printStackTrace();
            return false;
        }
    }
}
