package com.luminary.os.core;

import com.indvd00m.ascii.render.Region;
import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.Rectangle;
import com.indvd00m.ascii.render.elements.plot.Axis;
import com.indvd00m.ascii.render.elements.plot.AxisLabels;
import com.indvd00m.ascii.render.elements.plot.Plot;
import com.indvd00m.ascii.render.elements.plot.api.IPlotPoint;
import com.indvd00m.ascii.render.elements.plot.misc.PlotPoint;
import com.luminary.os.utils.Pair;
import lombok.Getter;
import com.luminary.os.OS;
import com.luminary.os.core.services.Service;
import com.luminary.os.events.ProcessTimeoutEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ProcessManager {
    private int currID = 0;
    private int elasped = 0;
    private static ProcessManager procManager;
    private Thread manager;
    private LinkedList<Pair<Integer, Integer>> processHistory = new LinkedList<>();
    private final Deque<Process> queuedProcess = new ArrayDeque<>();
    @Getter
    private final ConcurrentHashMap<Integer, Process> runningProcesses = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    /**
     * @see com.luminary.os.core.Priority
     */
    public void add(@NotNull Process t) {
        queuedProcess.add(t);
        List<Process> sorted = new ArrayList<>(queuedProcess);
        sorted.sort(Comparator.comparingInt(p -> p.getPriority().ordinal())); // sorts by the priority enum ordinal
        queuedProcess.clear();
        queuedProcess.addAll(sorted);
    }

    public Thread getProcess(int id) {
        return runningProcesses.get(id).getThread();

    }
    public void kill(int id) {
        Process proc = runningProcesses.get(id);
        if(proc != null) {
            currID--;
            proc.stop();
            runningProcesses.remove(id);
        } else {
            System.out.println("Unknown Process ID...");
        }
    }

    public void shutdown() {
        executor.shutdown();
        runningProcesses.forEach((k, v) -> { v.getThread().interrupt(); });
        runningProcesses.clear();
    }
    public String getRunning() {
        List<IPlotPoint> points = new ArrayList<>();
        for(Pair<Integer, Integer> pair : processHistory) {
            points.add(new PlotPoint(pair.getFirst(), pair.getSecond()));
        }
        IRender render = new Render();
        ICanvas canvas = render.render(render.newBuilder()
                .width(100)
                .height(30)
                .element(new Rectangle(0, 0, 80, 20))
                .layer(new Region(1, 1, 78, 18))
                .element(new Axis(points, new Region(0, 0, 78, 18)))
                .element(new AxisLabels(points, new Region(0, 0, 78, 18)))
                .element(new Plot(points, new Region(0, 0, 78, 18)))
                .build()
        );
        return canvas.getText();
    }
    public void start() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                if(processHistory.size() >= 100) processHistory.removeFirst();
                processHistory.push(new Pair<>(elasped, runningProcesses.size()));
                elasped++;
            } catch (InterruptedException e) {
                System.out.println("Error when trying to save processes");
            }
        }).start();
        executor.scheduleAtFixedRate(() -> {
            if (!queuedProcess.isEmpty()) {
                Process process = queuedProcess.poll();
                Thread thread = process.start();
                if(thread != null) {
                    runningProcesses.put(currID, process);
                    process.setId(currID);
                    currID++;
                    // Watcher
                    if(!(process instanceof Service)) {
                        new Thread(() -> {
                            try {
                                thread.join(process.getTimeoutMillis());
                                if (thread.isAlive()) {
                                    thread.interrupt();
                                    OS.getEventHandler().post(new ProcessTimeoutEvent(process, process.getId()));
                                    runningProcesses.remove(process.getId());
                                } else {
                                    runningProcesses.remove(process.getId());
                                }
                            }
                            catch (InterruptedException ignored) {}
                        }).start();
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    private ProcessManager() {}
    public static ProcessManager getProcessManager() {
        if(procManager == null) {
            procManager = new ProcessManager();
        }
        return procManager;
    }
}
