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

package com.luminary.os.core;


import lombok.Getter;

import java.io.File;
import java.util.Set;

public class Native {
    private static Native instance = null;

    @Getter
    private static boolean loaded = false;
    public static boolean supportsNative() { return isWindows() || isUnix(); }
    public static Native getInstance() {
        if (instance == null) { instance = new Native(); }
        return instance;
    }

    public static boolean isWindows() {
        return (System.getProperty("os.name").contains("win"));
    }

    public static boolean isUnix() {
        String o = System.getProperty("os.name");
        return (o.contains("nix") || o.contains("nux") || o.indexOf("aix") > 0);
    }

    private Native() {
        if(!supportsNative()) {
            throw new UnsupportedOperationException("LuminaryOS Native Optimisation is currently only supported on windows/unix!");
        }
        try {
            if(isWindows()) {
                System.loadLibrary("LuminaryOS/natives/windows");
            } else if (isUnix()) {
                System.loadLibrary("LuminaryOS" + File.separator + "natives" + File.separator + "linux");
            }
            loaded = true;
        } catch (Exception ignored) {
            System.out.println("Failed to load Native.");
        }

    }
    public native String getInfo();

    public native String blacklistMethods(Class<?> clazz, Set<String> blacklistMethods);
    public native String blacklistFields(Class<?> clazz, Set<String> blacklistFields);
    // Rendering
    public native float calculateX(int i, int j, int k, float A, float B, float C);
    public native float calculateY(int i, int j, int k, float A, float B, float C);
    public native float calculateZ(int i, int j, int k, float A, float B, float C);
}
