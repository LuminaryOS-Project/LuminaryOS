package me.intel.os.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.intel.os.core.Process;
import me.intel.os.core.StatusCode;

public class ProcessTimeoutEvent {
    @Getter
    private final Process process;
    @Getter
    private final int pID;
    public ProcessTimeoutEvent(Process process, int piD) {
        this.process = process;
        this.pID = piD;
        process.getCallback().ifPresent(callback -> { callback.accept(StatusCode.TIMEOUT); });
    }

}
