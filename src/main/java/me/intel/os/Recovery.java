package me.intel.os;

import java.io.File;
import java.util.HashMap;

public class Recovery {
    private final HashMap<File, String> Filehashes = new HashMap<>();
    public void start() {
        System.out.println("Recovery starting...");
    }
}
