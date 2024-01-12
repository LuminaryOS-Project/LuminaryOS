package com.luminary.os.core;

import com.luminary.os.permissions.PermissionLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    USER(PermissionLevel.USER),
    OS(PermissionLevel.ROOT),
    SYSTEM(PermissionLevel.SYSTEM);
    private PermissionLevel permission;
}
