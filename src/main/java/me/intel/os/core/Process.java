package me.intel.os.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class Process {
    @Getter
    private final @NotNull Thread thread;
    @Getter
    @Setter
    private Priority priority = Priority.NORMAL;
    @Getter
    private final long timeoutMillis;
    @Getter
    @Setter
    private int id;
    public Thread start() {
        if(!thread.isAlive()) {
            thread.setPriority(1);
            thread.start();
        }
        return thread;

    }
    public void stop() {
        if(thread.isAlive()) {
            thread.stop();
        }
    }

}