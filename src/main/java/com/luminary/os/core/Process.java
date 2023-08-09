package com.luminary.os.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
    @Getter
    private @NotNull Optional<Callback> callback = Optional.empty();
    public void setCallback(@NotNull Callback callback) {
        this.callback = Optional.of(callback);
    }
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
            callback.ifPresent(callback -> callback.accept(StatusCode.SUCCESS));
        }
    }


}