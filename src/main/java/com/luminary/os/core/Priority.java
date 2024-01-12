package com.luminary.os.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority {
    SYSTEM(10),
    HIGH(7),
    NORMAL(5),
    LOW(2);
    private int priorityValue;
}
