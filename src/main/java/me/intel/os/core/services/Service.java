package me.intel.os.core.services;

import lombok.Getter;
import me.intel.os.core.Callback;
import me.intel.os.core.Level;
import me.intel.os.core.Process;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public class Service extends Process {
    //////////////////////////////////////////
    //                States:               //
    // 0: Off                               //
    // 1: On                                //
    //////////////////////////////////////////
    @Getter
    public final boolean autoStart;
    @Getter
    private Optional<Callback> callback;
    public void setCallback(Callback callback) {
        this.callback = Optional.of(callback);
    }
    @Getter
    private Level level = Level.USER;
    public int startTrigger = 0;
    ////////////////////////////////////////////////////
    //                Triggers:
    // 0: Start before commands are registered
    // 1: Start after shell
    ////////////////////////////////////////////////
    public Service(@NotNull Thread thread,boolean autoStart,int startTrigger) {
        super(thread, 0);
        this.startTrigger = startTrigger;
        this.autoStart = autoStart;
    }
    public Service(@NotNull Thread thread, boolean autoStart, int startTrigger, Level level) {
        super(thread, 0);
        this.startTrigger = startTrigger;
        this.autoStart = autoStart;
        this.level = level;
    }
}
