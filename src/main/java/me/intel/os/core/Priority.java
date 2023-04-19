package me.intel.os.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Priority {
    SYSTEM(10),
    HIGH(7),
    NORMAL(5),
    LOW(2);
    @Getter
    private int priorityValue;
}
