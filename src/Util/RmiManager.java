package Util;

import lombok.Data;

import javax.swing.*;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@Data
public class RmiManager implements Serializable {
    private Registry registry;


    public Registry init(int port) {
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return registry;
        }
    }

    public void bind(String name, UnicastRemoteObject obj) {
        try {
            registry.bind(name, obj);
        } catch (Exception e) {
            new JDialog().add(new JLabel("Registry not initiated")).setVisible(true);
            e.printStackTrace();
        }
    }

    public Object getRemoteInterface(String host, int port, String handle) throws RemoteException, NotBoundException {
        return LocateRegistry.getRegistry(host, port).lookup(handle);
    }
}
