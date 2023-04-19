package me.intel.os.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
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
}
