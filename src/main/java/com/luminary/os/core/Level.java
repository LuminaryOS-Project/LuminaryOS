package com.luminary.os.core;

import com.luminary.os.permissions.PermissionLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Level {
    USER(PermissionLevel.USER),
    OS(PermissionLevel.ROOT),
    SYSTEM(PermissionLevel.SYSTEM);
    @Getter
    private PermissionLevel permission;
}
