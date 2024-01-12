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

package com.luminary.os;

import com.luminary.os.utils.Log;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Recovery {
    private final HashMap<String, File> fileHashes = new HashMap<>();
    public void start() {
        System.out.println("Recovery starting...");
    }
    public void check(String path) {
        File folder = new File(path + "/");
        ArrayList<File> files = new ArrayList<>(Arrays.asList(folder.listFiles()));
        files.stream().filter(File::isFile).forEach(file -> {
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
