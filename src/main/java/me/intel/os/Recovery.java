package me.intel.os;

import me.intel.os.utils.Log;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class Recovery {
    private final HashMap<File, String> Filehashes = new HashMap<>();
    public void start() {
        System.out.println("Recovery starting...");
    }
    public static CompletableFuture<String> computeHashAsync(File file) {
        return CompletableFuture.supplyAsync(() -> {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
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
