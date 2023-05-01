package com.luminary.os.commands;

import com.luminary.os.core.User;
import com.luminary.os.permissions.PermissionLevel;
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
