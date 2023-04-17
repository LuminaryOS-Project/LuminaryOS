package me.intel.os;

import me.intel.os.utils.Log;
import me.intel.os.utils.Requests;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Recovery {
    private final HashMap<String, File> fileHashes = new HashMap<>();
    public void start() {
        System.out.println("Recovery starting...");
    }
    public void check(String path) {
        File folder = new File(path + "/");
        ArrayList<File> files = new ArrayList<>(Arrays.asList(folder.listFiles()));;
        files.stream().filter(f -> f.isFile()).forEach(file -> {
            try {
                fileHashes.put(computeHashAsync(file).get(), file);
            } catch (InterruptedException | ExecutionException ignored) {}
        });
        //System.out.println(fileHashes.toString());

    }
    public static CompletableFuture<String> computeHashAsync(File file) {
        return CompletableFuture.supplyAsync(() -> {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
                byte[] hashBytes = digest.digest();
                StringBuilder hashBuilder = new StringBuilder();
                for (byte b : hashBytes) {
                    hashBuilder.append(String.format("%02x", b));
                }
                return hashBuilder.toString();
            } catch (Exception e) {
                Log.error("Failed to compute file hash..");
                e.printStackTrace();
                return null;
            }
        });
    }
}
