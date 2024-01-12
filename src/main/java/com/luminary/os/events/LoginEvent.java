package com.luminary.os.events;

import com.luminary.os.permissions.PermissionLevel;
import lombok.Getter;

@Getter
public class LoginEvent extends UserEvent {
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
