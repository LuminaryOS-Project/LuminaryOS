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

import com.luminary.os.utils.FileLogger;
import com.luminary.os.utils.Log;

import java.util.Set;

public class LuminarySecurityManager {
    public static void blockFields(Class<?> clazz, Set<String> fields) {
        if(fields.isEmpty())
            return;
        //
        FileLogger.log("-".repeat(32));
        FileLogger.log(Log.getLogMessage("SECURITY", "Blocking fields \"" + fields + "\" for class " + clazz.getName()));
        FileLogger.log(Log.getLogMessage("SECURITY", "Response from native method: " + Native.getInstance().blacklistFields(clazz, fields)));
        //
        FileLogger.log("-".repeat(32));
    }
    public static void blockMethods(Class<?> clazz, Set<String> methods) {
        if(methods.isEmpty())
            return;
        //
        FileLogger.log("-".repeat(32));
        FileLogger.log(Log.getLogMessage("SECURITY", "Blocking methods \"" + methods + "\" for class " + clazz.getName()));
        FileLogger.log(Log.getLogMessage("SECURITY", "Response from native method: " + Native.getInstance().blacklistMethods(clazz, methods)));
        //
        FileLogger.log("-".repeat(32));
    }
}
