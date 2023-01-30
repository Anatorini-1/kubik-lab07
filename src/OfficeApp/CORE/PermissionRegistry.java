package OfficeApp.CORE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PermissionRegistry {
    private HashMap<ClubModel, ArrayList<String>> permissions;
    private ReadWriteLock lock;
    private ClubRegistry clubRegistry;

    public PermissionRegistry(ClubRegistry clubRegistry) {
        permissions = new HashMap<>();
        lock = new ReentrantReadWriteLock();
        this.clubRegistry = clubRegistry;
    }

    public boolean requestPermission(String clubName, String sector) {
        try {
            lock.writeLock().lock();
            permissions.putIfAbsent(clubRegistry.getClub(clubName), new ArrayList<>());
            if (checkPermission(sector) != null) return false;
            ArrayList<String> activePermissions = permissions.getOrDefault(clubRegistry.getClub(clubName), new ArrayList<>());
            int permissionCount = activePermissions.size();
            if (permissionCount < 2) {
                activePermissions.add(sector);
                System.out.println("Permission granted to " + clubName + " for sector " + sector);
                return true;
            } else if (permissionCount == 2) {
                if (activePermissions.contains(sector)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } finally {
            lock.writeLock().unlock();
        }

    }

    public boolean endPermission(String clubName, String sector) {
        try {
            lock.writeLock().lock();
            ArrayList<String> activePermissions = permissions.get(clubRegistry.getClub(clubName));
            if (activePermissions.contains(sector)) {
                activePermissions.remove(sector);
                System.out.println("Permission ended for " + clubName + " for sector " + sector);
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean checkPermission(String clubName, String sector) {
        try {
            lock.readLock().lock();
            ArrayList<String> activePermissions = permissions.get(clubName);
            return activePermissions.contains(sector);
        } catch (NullPointerException e) {
            return false;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void removeClub(String clubName) {
        try {
            lock.writeLock().lock();
            permissions.remove(clubRegistry.getClub(clubName));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ClubModel checkPermission(String sector) {
        try {
            lock.readLock().lock();
            for (ClubModel clubModel : permissions.keySet()) {
                if (permissions.getOrDefault(clubModel, new ArrayList<>()).contains(sector)) {
                    return clubModel;
                }
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void printPermissions() {
        try {
            lock.readLock().lock();
            for (ClubModel clubModel : permissions.keySet()) {
                //System.out.println(clubModel.getName() + " has permissions for " + permissions.get(clubModel));
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public ArrayList<String> getClubPermissions(String name) {
        try {
            lock.readLock().lock();
            return permissions.getOrDefault(clubRegistry.getClub(name), new ArrayList<>());
        } finally {
            lock.readLock().unlock();
        }
    }
}
