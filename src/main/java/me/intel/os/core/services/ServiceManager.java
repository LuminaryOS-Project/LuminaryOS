package me.intel.os.core.services;

import com.google.common.eventbus.Subscribe;
import me.intel.os.OS;
import me.intel.os.core.ProcessManager;
import me.intel.os.events.AfterShellEvent;
import me.intel.os.events.BeforeCommandRegisterEvent;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceManager {
    private static ServiceManager serviceManager;

    // Integer: sID, Service: service instance
    private static ConcurrentHashMap<Integer,Service> services = new ConcurrentHashMap<>();

    private static int sIDcntr = 0;

    public void RegisterService(Service service){ // @TODO: Add more arguments
        services.put(sIDcntr,service);
        sIDcntr++;
    }

    public void registerEvents(){
        OS.getEventHandler().register(this);
    }

    @Subscribe
    public void trigger0EventHandler(BeforeCommandRegisterEvent e){
        services.forEach((k,v) -> {
            if(v.autoStart && v.startTrigger == 0 && !v.getThread().isAlive()){
                v.start();
            }
        });
    }

    @Subscribe
    public void trigger1EventHandler(AfterShellEvent e){
        services.forEach((k,v) -> {
            if(v.autoStart && v.startTrigger == 1 && !v.getThread().isAlive()){
                v.start();
            }
        });
    }

    private boolean isRegistered(Service service){
        return services.contains(service);
    }

    public boolean startService(Service service ){
        if(isRegistered(service) && !service.getThread().isAlive()){
            System.out.println("Starting service " + service.getThread().getName());
            try {
                service.start();
            } catch (IllegalThreadStateException e ){
                e.printStackTrace();
                return false;
            }
            return true;
        } else if(service.getThread().isAlive()) {
            System.out.println("Failed to start service (Service is already running)");
            return false;
        } else {
            System.out.println("Failed to start service (No such service)");
            return false;
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

    public ServiceManager() {}
    public static ServiceManager getServiceManager() {
        if(serviceManager == null) {
            serviceManager = new ServiceManager();
        }
        return serviceManager;
    }
}
