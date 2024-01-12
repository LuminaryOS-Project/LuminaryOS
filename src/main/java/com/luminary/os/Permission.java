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

package com.luminary.os;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Permission {
    ADMINISTRATOR(0x73D00B7B),
    DELETE_FILE(0x5AA8C685),
    CREATE_USER(0x63DF8907),
    DELETE_USER(0x4E7C126B),
    NONE(0x000000);
    private final int code;
    public static boolean hasPermission(int permissionCode, Permission permission) {
        return ((permissionCode & permission.getCode()) == permission.getCode());
    }

    public static int from(Permission... permissions) {
        int code = 0x00;
        for (Permission permission : permissions) {
            code |= permission.getCode();
        }
        return code;
    }
    public static int add(int code, Permission permission) {
        return code | permission.getCode();
    }
    public static Permission of(int code) {
        for(Permission p : Permission.values()) {
            if(p.getCode() == code) {
                return p;
            }
        }
        return Permission.NONE;
    }
    public static List<Permission> has(int code) {
        List<Permission> sp = new ArrayList<>();
        for (Permission permission : Permission.values()) {
            if ((code & permission.getCode()) == permission.getCode()) {
                sp.add(permission);
            }
        }
        return sp;
    }
}
