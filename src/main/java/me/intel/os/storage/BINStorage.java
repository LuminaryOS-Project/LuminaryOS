package me.intel.os.storage;

import lombok.AllArgsConstructor;

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

    private static byte[] readFromFile(DataInputStream inputStream, String fileName) throws IOException {
        boolean fileFound = false;
        byte[] buffer = null;
        while (!fileFound) {
            try {
                String currentFileName = inputStream.readUTF();
                int length = inputStream.readInt();
                if (currentFileName.equals(fileName)) {
                    buffer = new byte[length];
                    inputStream.readFully(buffer);
                    fileFound = true;
                } else { inputStream.skipBytes(length); }
            } catch (EOFException e) { break; }
        }
        if (!fileFound) { throw new FileNotFoundException("File not found: " + fileName); }
        return buffer;
    }
}


