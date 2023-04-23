package me.intel.os.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.intel.os.permissions.PermissionLevel;

@AllArgsConstructor
public enum Level {
    USER(PermissionLevel.USER),
    OS(PermissionLevel.ROOT),
    SYSTEM(PermissionLevel.SYSTEM);
    @Getter
    private PermissionLevel permission;
}
