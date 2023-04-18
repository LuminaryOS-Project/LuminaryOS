package me.intel.os.utils;
import me.intel.os.core.Color;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Objects;


public class Log {
    // allows chaining
    private static Log log;
    public static Log info(@NotNull Object o) {
        LocalTime time = LocalTime.now();
        System.out.printf("[%02d:%02d:%02d] [%s]: %s%n",
                time.getHour(), time.getMinute(), time.getSecond(), "INFO", o);
        return getInstance();
    }
    public static Log warn(@NotNull Object o) {
        LocalTime time = LocalTime.now();
        System.out.printf("[%02d:%02d:%02d] [%s]: %s%n",
                time.getHour(), time.getMinute(), time.getSecond(), "WARN", o);
        return getInstance();
    }
    public static Log error(@NotNull Object o) {
        LocalTime time = LocalTime.now();
        System.out.printf(Color.RED + "[%02d:%02d:%02d] [%s]: %s%n" + Color.RESET,
                time.getHour(), time.getMinute(), time.getSecond(), "ERROR", o);
        return getInstance();
    }
    private Log() {}
    public static Log getInstance() {
        return Objects.requireNonNullElseGet(log, Log::new);
    }
}
