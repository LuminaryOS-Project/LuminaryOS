package me.intel.os.utils;
import me.intel.os.core.Color;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;


public class Log {
    public static void info(@NotNull Object o) {
        LocalTime time = LocalTime.now();
        System.out.printf("[%02d:%02d:%02d]: [%s]: %s%n",
                time.getHour(), time.getMinute(), time.getSecond(), "INFO", o);
    }
    public static void warn(@NotNull Object o) {
        LocalTime time = LocalTime.now();
        System.out.printf("[%02d:%02d:%02d]: [%s]: %s%n",
                time.getHour(), time.getMinute(), time.getSecond(), "WARN", o);
    }
    public static void error(@NotNull Object o) {
        LocalTime time = LocalTime.now();
        System.out.printf(Color.RED + "[%02d:%02d:%02d] [%s]: %s%n" + Color.RESET,
                time.getHour(), time.getMinute(), time.getSecond(), "ERROR", o);
    }
}
