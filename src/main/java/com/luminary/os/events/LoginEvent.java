/*
 * Copyright (c) 2024. Intel
 *
 * This file is part of LuminaryOS
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
