package com.luminary.os.core.services;

import com.luminary.os.OS;
import com.luminary.os.core.StatusCode;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceManager {
    private static ServiceManager serviceManager;

    // Integer: sID, Service: service instance
    private static final ConcurrentHashMap<Integer,Service> services = new ConcurrentHashMap<>();

    private static int sIDcntr = 0;

    public void RegisterService(Service service){ // @TODO: Add more arguments
        services.put(sIDcntr,service);
        sIDcntr++;
    }

    private boolean isRegistered(Service service){
        return services.containsValue(service) || services.containsKey(service);
    }

    public void startService(Service service) {
        if (isRegistered(service)) {
            Thread thread = service.getThread();
            if (!thread.isAlive()) {
                System.out.println("Starting service " + thread.getName());
                if(!OS.getCurrentUser().getPermissionLevel().canPerformAction(service.getLevel())) {
                    service.getCallback().ifPresent(callback -> callback.accept(StatusCode.INSUFFICIENT_PERMISSION));
                    System.out.println("Not enough permissions");
                    return;
                }
                service.getCallback().ifPresent(callback -> callback.accept(StatusCode.SUCCESS));
                try {
                    thread.start();
                } catch (IllegalThreadStateException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to start service (Service is already running)");
            }
        } else {
            System.out.println("Failed to start service (No such service)");
        }
    }
    public boolean stopService(Service service) {
        if (isRegistered(service) && service.getThread().isAlive()) {
            System.out.println("Stopped service " + service.getThread().getName());
            service.stop();
            return true;
        } else if(!service.getThread().isAlive()) {
            System.out.println("Failed to stop service (Service is not running)");
            return false;
        } else {
            System.out.println("Failed to stop service (No such service)");
            return false;
        }
    }

    private ServiceManager() {}
    public static ServiceManager getServiceManager() {
        if(serviceManager == null) {
            serviceManager = new ServiceManager();
        }
        return serviceManager;
    }
}
