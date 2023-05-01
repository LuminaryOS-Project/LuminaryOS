package com.luminary.os.events;

import lombok.Getter;
import com.luminary.os.core.Process;
import com.luminary.os.core.StatusCode;

public class ProcessTimeoutEvent {
    @Getter
    private final Process process;
    @Getter
    private final int pID;
    public ProcessTimeoutEvent(Process process, int piD) {
        this.process = process;
        this.pID = piD;
        process.getCallback().ifPresent(callback -> callback.accept(StatusCode.TIMEOUT));
    }

}
