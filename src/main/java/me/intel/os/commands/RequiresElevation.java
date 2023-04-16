package me.intel.os.commands;

import me.intel.os.core.User;
import me.intel.os.permissions.PermissionLevel;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public interface RequiresElevation {
    default boolean success(User u) {
        if(u.getPermissionLevel().equals(PermissionLevel.ADMIN) || u.getPermissionLevel().equals(PermissionLevel.ROOT)) {
            return true;
        }
        return false;
    }
}
