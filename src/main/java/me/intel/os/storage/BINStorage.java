package me.intel.os.storage;

import lombok.AllArgsConstructor;
import me.intel.os.OS;

import java.io.*;

@AllArgsConstructor
public class BINStorage {
    private String output;
    /*
            Shows how to read and write data
    public static void main(String[] args) {

        // Write files to binary file
        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream("output"))) {
            writeToFile("file1", outputStream);
            writeToFile("file2", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream("output"))) {
            System.out.println(new String(readFromFile(inputStream, "file")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private static void writeToFile(String fileName, DataOutputStream outputStream) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            byte[] buffer = new byte[(int) file.length()];
            try (FileInputStream inputStream = new FileInputStream(file)) {
                inputStream.read(buffer);
                outputStream.writeUTF(fileName);
                outputStream.writeInt(buffer.length);
                outputStream.write(buffer);
            }
        }
    }

    private static Object readFromFile(DataInputStream inputStream, String fileName) throws IOException {
        boolean fileFound = false;
        Object result = null;
        while (!fileFound) {
            try {
                String currentFileName = inputStream.readUTF();
                int length = inputStream.readInt();
                if (currentFileName.equals(fileName)) {
                    byte[] buffer = new byte[length];
                    inputStream.readFully(buffer);
                    result = buffer;
                    fileFound = true;
                } else {
                    inputStream.skipBytes(length);
                }
            } catch (EOFException e) {
                break;
            }
        }
        if (!fileFound) {
            throw new FileNotFoundException(OS.getLanguage().get("fileNotFound") + fileName);
        }
        return result;
    }
}


