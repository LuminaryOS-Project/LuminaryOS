package me.intel.os.core;

import java.util.HashMap;

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
                }
            } catch (InterruptedException ignored) {}
        }).start();
        runningProcesses.put(currID, t);
        currID++;
    }
    @SuppressWarnings({"deprecation"})
    public void kill(int id) {
        runningProcesses.get(id).stop();
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
