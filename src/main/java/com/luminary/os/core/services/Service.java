package com.luminary.os.core.services;

import com.luminary.os.core.Callback;
import com.luminary.os.core.Level;
import com.luminary.os.core.Process;
import lombok.Getter;
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
    private @NotNull Optional<Callback> callback;
    public void setCallback(@NotNull Callback callback) {
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
    public Service(@NotNull Thread thread, boolean autoStart, int startTrigger) {
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
