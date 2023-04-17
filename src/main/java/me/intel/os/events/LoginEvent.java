package me.intel.os.events;

import lombok.Getter;
import me.intel.os.permissions.PermissionLevel;

public class LoginEvent extends UserEvent {
    @Getter
    private final PermissionLevel permissionLevel;

    /**
     * <h3>Default event constructor</h3>
     * @param username Username of the user logging in
     * @param pL Permission Level of the user logging in
     */
    public LoginEvent(String username, PermissionLevel pL) {
        super(username);
        this.permissionLevel = pL;
    }
}
