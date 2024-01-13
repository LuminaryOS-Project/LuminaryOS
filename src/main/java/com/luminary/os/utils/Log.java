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

package com.luminary.os.utils;
import com.luminary.os.OS;
import com.luminary.os.core.Color;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.time.LocalTime;
import java.util.Objects;


public class Log {
    // allows chaining
    private static Log log;
    private static FileWriter fw;
    public static Log info(@NotNull Object o, boolean stdout) {
        LocalTime time = LocalTime.now();
        String msg = String.format("[%02d:%02d:%02d | %s] [%s]: %s%n",
                time.getHour(), time.getMinute(), time.getSecond(), getCallerInfo(), OS.getLanguage() != null ? OS.getLanguage().get("info") : "INFO", o);
        FileLogger.log(msg);
        if(stdout)
            System.out.println(msg);
        return getInstance();
    }
    public static Log info(@NotNull Object o) {
        return info(o, true);
    }
    public static Log warn(@NotNull Object o, boolean stdout) {
        LocalTime time = LocalTime.now();
        String msg = String.format("[%02d:%02d:%02d | %s] [%s]: %s%n",
                time.getHour(), time.getMinute(), time.getSecond(), getCallerInfo(), OS.getLanguage() != null ? OS.getLanguage().get("warn") : "WARN", o);
        FileLogger.log(msg);
        if(stdout)
            System.out.println(msg);
        return getInstance();
    }

    public static Log warn(@NotNull Object o) {
        return warn(o, true);
    }

    public static Log error(@NotNull Object o, boolean stdout) {
        LocalTime time = LocalTime.now();
        String msg = String.format("[%02d:%02d:%02d | %s] [%s]: %s%n" ,
                time.getHour(), time.getMinute(), time.getSecond(), getCallerInfo(), OS.getLanguage() != null ? OS.getLanguage().get("error") : "ERROR", o);
        FileLogger.log(msg);
        if(stdout)
            System.out.println(Color.RED + msg + Color.RESET);
        return getInstance();
    }
    public static Log error(@NotNull Object o) {
        return error(o, true);
    }

    public static String getLogMessage(String logLevel, @NotNull Object o) {
        LocalTime time = LocalTime.now();
        return String.format("[%02d:%02d:%02d | %s] [%s]: %s%n",
                time.getHour(), time.getMinute(), time.getSecond(), getCallerInfo(), logLevel, o);
    }

    private Log() {}
    private static String getCallerInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length >= 4) {
            StackTraceElement callerElement = stackTrace[3];
            String fileName = callerElement.getFileName();
            int lineNumber = callerElement.getLineNumber();
            return String.format("%s:%d", fileName, lineNumber);
        }
        return "";
    }
    public static Log getInstance() {
        return Objects.requireNonNullElseGet(log, Log::new);
    }
}
