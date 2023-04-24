package me.intel.os.utils;

import me.intel.os.OS;

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
    public static String encodeBase64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
    public static String decodeBase64(String input) {
        return new String(Base64.getDecoder().decode(input.getBytes()));
    }
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
    public static File[] listDirectories(String path) {
        return new File(path).listFiles(File::isDirectory);
    }
    public static File[] listFilesInDirectory(final File folder) {
        return folder.listFiles(File::isFile);
    }
    public static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            Arrays.stream(Objects.requireNonNull(directory.listFiles())).forEach(Utils::deleteDirectory);
        }
        directory.delete();
    }
    public static void createDirectory(Path path) {
        new File(path.toString()).mkdirs();
    }
    public static void clearScreen() {
        System.out.println("If you can see this message your terminal doesnt support the clearing ASCI Escape code!");
        if(System.getProperty("os.name").contains("Windows")) {
            System.out.println("You seem to be on windows, we recommend you use the new Windows Terminal App!");
            System.out.println("https://github.com/microsoft/terminal");
        } else {
            System.out.println("Most linux / macOS terminals should support this escape code!");
        }
        System.out.println("\033\143");
    }
    public static String MD5Hash(String file) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(file));
            byte[] hash = MessageDigest.getInstance("MD5").digest(data);
            return new BigInteger(1, hash).toString(16);
        } catch (Exception ignored) {}
        return null;
    }
    public static void createDisk(int i) throws IOException {
        Path diskPath = Paths.get("IntelOS", "disks", String.valueOf(i));
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
                    cfg.set("INTELOS_DATA.version", 1.0);
                }
                try(JSONConfig cfg = new JSONConfig(Paths.get("IntelOS", "cache", "cache.json").toString())) {
                    cfg.set("disks.0.hash", hash);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(OS.getLanguage().get("diskCantBeCreated"), e);
            }
        } else {
            throw new IllegalArgumentException(OS.getLanguage().get("diskAlreadyExists"));
        }
    }
}
