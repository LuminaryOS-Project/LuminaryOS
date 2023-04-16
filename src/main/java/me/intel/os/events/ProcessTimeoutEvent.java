package me.intel.os.events;

import lombok.Getter;

public class ProcessTimeoutEvent {
    @Getter
    private final Thread thread;
    @Getter
    private final int pID;
    public ProcessTimeoutEvent(Thread thread, int piD){
        this.thread = thread;
        this.pID = piD;
    }

}
