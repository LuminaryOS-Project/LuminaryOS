package me.intel.os.core;

import org.jetbrains.annotations.NotNull;

public class Service extends Process {
    public Service(@NotNull Thread thread) {
        super(thread, 0);
    }
}
