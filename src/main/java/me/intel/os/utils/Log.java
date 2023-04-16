package me.intel.os.utils;
import java.time.LocalTime;


public class Log {
    public void info(Object o) {
        LocalTime time = LocalTime.now();
        System.out.println(String.format("[%02d:%02d:%02d]: [%s]: %s",
                time.getHour(), time.getMinute(), time.getSecond(), "Test", message));
    }
    public void warn(Object o) {
        LocalTime time = LocalTime.now();
        System.out.println(String.format("[%02d:%02d:%02d]: [%s]: %s",
                time.getHour(), time.getMinute(), time.getSecond(), "Test", message));
    }
    public void error(Object o) {
        LocalTime time = LocalTime.now();
        System.out.println(String.format("[%02d:%02d:%02d]: [%s]: %s",
                time.getHour(), time.getMinute(), time.getSecond(), "Test", message));
    }
}
