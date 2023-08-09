package com.luminary.os.utils;

import com.github.luben.zstd.Zstd;
import com.google.gson.GsonBuilder;
import com.luminary.os.OS;
import com.luminary.os.utils.network.Request;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {
    /**
     * <h3>Encodes a string to Base64</h3>
     * @param input String to encode
     * @return Encoded String
     */
    public static String encodeBase64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    /**
     * <h3>Decodes a Base64 string</h3>
     * @param input String to decode
     * @return Decoded String
     */
    public static String decodeBase64(String input) {
        return new String(Base64.getDecoder().decode(input.getBytes()));
    }

    /**
     * <h3>Compresses Data Using ZSTD</h3>
     * @param data byte[] of data to compress
     * @return Compressed Data
     */
    public static byte[] compress(byte[] data) {
        return Zstd.compress(data);
    }
    /**
     * <h3>Compresses Data Using ZSTD</h3>
     * @param data String of data to compress
     * @return Compressed Data
     */
    public static byte[] compress(String data) {
        return Zstd.compress(data.getBytes());
    }

    /**
     * <h3>Decompresses ZSTD Data</h3>
     * @param data Data to decompress
     * @return Decompressed byte[] Data
     */
    public static byte[] decompress(byte[] data) {
        byte[] des = new byte[data.length];
        Zstd.decompress(des, data);
        return des;
    }
    /**
     * <h3>Zips files using ZipOutputStream</h3>
     * @param srcFilenames Files to zip
     * @param zipFilename Output zip file name
     * @throws IOException
     */
    public static void zipFiles(String[] srcFilenames, String zipFilename) throws IOException {
        try (
                FileOutputStream fileOut = new FileOutputStream(zipFilename);
                ZipOutputStream zipOut = new ZipOutputStream(fileOut)
        ) {
            for (String srcFilename : srcFilenames) {
                File srcFile = new File(srcFilename);
                try (FileInputStream fileIn = new FileInputStream(srcFile)) {
                    ZipEntry zipEntry = new ZipEntry(srcFile.getName());
                    zipOut.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fileIn.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                }
            }
        }
    }

    /**
     * <h3>Lists all the folders within a folder</h3>
     * @param path Path to list
     * @return File[]
     */
    public static File[] listDirectories(String path) {
        return new File(path).listFiles(File::isDirectory);
    }
    /**
     * <h3>Lists all the files within a folder</h3>
     * @param folder Path to list
     * @return File[]
     */
    public static File[] listFilesInDirectory(final File folder) {
        return folder.listFiles(File::isFile);
    }

    /**
     * <h3>Deletes a tree of directories</h3>
     * @param directory Directory to delete
     */
    public static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            Arrays.stream(Objects.requireNonNull(directory.listFiles())).forEach(Utils::deleteDirectory);
        }
        directory.delete();
    }

    /**
     * <h3>Creates a directory from a path</h3>
     * @param path Path to create
     */
    public static void createDirectory(Path path) {
        new File(path.toString()).mkdirs();
    }

    /**
     * <h3>Creates a directory from a path</h3>
     * @param path Path to create
     */
    public static void createDirectory(String path) {
        new File(path).mkdirs();
    }

    /**
     * <h3>Clears the console or shows a warning if the users console doesn't support it</h3>
     */
    public static void clearScreen() {
        System.out.println("\nIf you can see this message your terminal doesnt support the clearing ASCI Escape code!");
        if(System.getProperty("os.name").contains("Windows")) {
            System.out.println("You seem to be on windows, we recommend you use the new Windows Terminal App!");
            System.out.println("https://github.com/microsoft/terminal \n");
        } else {
            System.out.println("Most linux / macOS terminals should support this escape code\n");
        }
        System.out.println("\033\143");
    }
    public Path getTempFolder() throws IOException {
        return Files.createTempDirectory(Paths.get("LuminaryOS", "temp"), "os_tmp");
    }
    /**
     * <h3>Gets the MD5 hash of a file</h3>
     * @param file The path to the file
     * @return MD5 hash of the file
     */
    public static String MD5Hash(String file) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(file));
            byte[] hash = MessageDigest.getInstance("MD5").digest(data);
            return new BigInteger(1, hash).toString(16);
        } catch (Exception ignored) {}
        return null;
    }
    public static Class<?> or(Class<?> c, Class<?> c1) {
        return c == null ? c1 : c;
    }
    public static boolean isUpToDate() {
        Request.get("https://api.github.com/repos/LuminaryOS-Project/LuminaryOS/releases/latest", null).then(response -> {
            try {
                JSONObject jres = (JSONObject) new JSONParser().parse(response.getBody());
                System.out.println("Ver?: " + jres.get("tag_name"));
            } catch (ParseException ignored) {
            }
            return response;
        });
        return false;
    }
    /**
     * <h3>Creates a folder and files for a disk</h3>
     * @param i Disk Number
     * @throws IOException
     */
    public static void createDisk(int i) throws IOException {
        Path diskPath = Paths.get("LuminaryOS", "disks", String.valueOf(i));
        if (Files.notExists(diskPath)) {
            Files.createDirectories(diskPath);
            Path binPath = diskPath.resolve("disk.bin");
            Path jsonPath = diskPath.resolve("disk.json");
            try {
                Files.createFile(binPath);
                Files.createFile(jsonPath);
                System.out.println("Successfully created disk!");
                String hash = MD5Hash(jsonPath.toString());
                try (JSONConfig cfg = new JSONConfig(jsonPath.toString())) {
                    cfg.set("lastHash", hash);
                    cfg.set("lastAccess", System.currentTimeMillis());
                    cfg.set("LuminaryOS_DATA.version", 1.0);
                }
                try(JSONConfig cfg = new JSONConfig(Paths.get("LuminaryOS", "cache", "cache.json").toString())) {
                    cfg.set("disks.0.hash", hash);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(OS.getLanguage().get("diskCantBeCreated"), e);
            }
        } else {
            throw new IllegalArgumentException(OS.getLanguage().get("diskAlreadyExists"));
        }
    }

    public static void delay(Runnable task, int ms) {
        new Thread(() -> {
            try {
                Thread.sleep(ms);
                task.run();
            } catch (InterruptedException e) {
                System.out.println("Error occurred while trying to delay task.");
            }
        }).start();
    }
}
