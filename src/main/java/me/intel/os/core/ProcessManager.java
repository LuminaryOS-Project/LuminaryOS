package me.intel.os.core;

import com.google.common.eventbus.Subscribe;
import me.intel.os.OS;
import me.intel.os.events.ProcessTimeoutEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProcessManager {
    private int currID = 0;
    private static ProcessManager procManager;
    private final HashMap<Integer, Thread> runningProcesses = new HashMap<>();
    public void add(Thread t) {
        runningProcesses.put(currID, t);
        currID++;
    }

    public void add(Thread t, int timeout) {
        // Watcher
        new Thread(() -> {
            try {
                t.join(timeout);
                if (t.isAlive()) {
                    t.interrupt();
                    runningProcesses.forEach((k, v) -> {
                        if (Objects.equals(t, v)) {
                            ProcessTimeoutEvent pTimeoutEvent = new ProcessTimeoutEvent(t, k);
                            OS.getEventHandler().post(pTimeoutEvent);
                        }
                    });
                    runningProcesses.entrySet().removeIf(entry -> Objects.equals(t, entry.getValue()));
                }
            }
            catch (InterruptedException ignored) {}
        }).start();
        runningProcesses.put(currID, t);
        currID++;
    }
    public String getProcess(int id) {
        return runningProcesses.get(id).getName();

    }

    @SuppressWarnings({"deprecation"})
    public void kill(int id) {
        runningProcesses.get(id).stop();
    }

    public void shutdown() {
        runningProcesses.forEach((k, v) -> {
            runningProcesses.get(k).interrupt();
        });
        runningProcesses.clear();
    }
    private ProcessManager() {


    }
    public static ProcessManager getProcessManager() {
        if(procManager == null) {
            procManager = new ProcessManager();
        }
        return procManager;
    }
}
