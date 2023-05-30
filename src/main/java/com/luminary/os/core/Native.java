package com.luminary.os.core;


public class Native {
    private static Native instance = null;

    private static boolean loaded = false;
    public static boolean isLoaded() {
        return loaded;
    }
    public static Native getInstance() {
        if (instance == null) { instance = new Native(); }
        return instance;
    }

    private Native() {
        if(!System.getProperty("os.name").toLowerCase().contains("windows")) {
            throw new UnsupportedOperationException("LuminaryOS Native Optimisation is currently only supported on windows!");
        }
        try {
            System.loadLibrary("LuminaryOS/natives/windows");
            loaded = true;
        } catch (Exception ignored) {
            System.out.println("Failed to load Windows Native.");
        }

    }
    public native String getInfo();
}
