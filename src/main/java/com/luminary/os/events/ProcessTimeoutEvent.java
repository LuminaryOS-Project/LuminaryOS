package com.luminary.os.events;

import com.luminary.os.core.Process;
import com.luminary.os.core.StatusCode;
import lombok.Getter;

@Getter
public class ProcessTimeoutEvent {
    private final Process process;
    private final int pID;
    public ProcessTimeoutEvent(Process process, int piD) {
        this.process = process;
        this.pID = piD;
        process.getCallback().ifPresent(callback -> callback.accept(StatusCode.TIMEOUT));
    }

}
