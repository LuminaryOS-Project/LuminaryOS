package com.luminary.os.core;


public class Native {
    private static Native instance = null;

    private static boolean loaded = false;
    public static boolean isLoaded() {
        return loaded;
    }
    public static boolean supportsNativeAccel() { return System.getProperty("os.name").contains("windows"); }
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
    // Rendering
    public native float calculateX(int i, int j, int k, float A, float B, float C);
    public native float calculateY(int i, int j, int k, float A, float B, float C);
    public native float calculateZ(int i, int j, int k, float A, float B, float C);
}
