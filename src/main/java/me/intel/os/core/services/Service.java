package me.intel.os.core.services;

import lombok.Getter;
import me.intel.os.core.Process;
import org.jetbrains.annotations.NotNull;

public class Service extends Process {
    //////////////////////////////////////////
    //                States:               //
    // 0: Off                               //
    // 1: On                                //
    //////////////////////////////////////////
    @Getter
    public boolean autoStart = false;

    ////////////////////////////////////////////////////
    //                Triggers:
    // 0: Start before commands are registered
    // 1: Start after shell
    ////////////////////////////////////////////////
    public int startTrigger = 0;
    public Service(@NotNull Thread thread,boolean autoStart,int startTrigger) {
        super(thread, 0);
        this.startTrigger = startTrigger;
        this.autoStart = autoStart;
    }
}
