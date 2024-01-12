package com.luminary.os.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class Process {
    private final @NotNull Thread thread;
    @Setter
    private Priority priority = Priority.NORMAL;
    private final long timeoutMillis;
    @Setter
    private int id;
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