package me.intel.os.core;

import org.jetbrains.annotations.NotNull;

public class Service extends Process {
    public Service(@NotNull Thread thread, long timeoutMillis) {
        super(thread, 0);
    }
}
