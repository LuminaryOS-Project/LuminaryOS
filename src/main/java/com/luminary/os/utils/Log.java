package com.luminary.os.utils;
import com.luminary.os.OS;
import com.luminary.os.core.Color;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Objects;


public class Log {
    // allows chaining
    private static Log log;
    public static Log info(@NotNull Object o) {
        LocalTime time = LocalTime.now();
        System.out.printf("[%02d:%02d:%02d | %s] [%s]: %s%n",
                time.getHour(), time.getMinute(), time.getSecond(), getCallerInfo(), OS.getLanguage() != null ? OS.getLanguage().get("info") : "INFO", o);
        return getInstance();
    }
    public static Log warn(@NotNull Object o) {
        LocalTime time = LocalTime.now();
        System.out.printf("[%02d:%02d:%02d | %s] [%s]: %s%n",
                time.getHour(), time.getMinute(), time.getSecond(), getCallerInfo(), OS.getLanguage() != null ? OS.getLanguage().get("warn") : "WARN", o);
        return getInstance();
    }
    public static Log error(@NotNull Object o) {
        LocalTime time = LocalTime.now();
        System.out.printf(Color.RED + "[%02d:%02d:%02d | %s] [%s]: %s%n" + Color.RESET,
                time.getHour(), time.getMinute(), time.getSecond(), getCallerInfo(), OS.getLanguage() != null ? OS.getLanguage().get("error") : "ERROR", o);
        return getInstance();
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
