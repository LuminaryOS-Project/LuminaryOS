package me.intel.os.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JSONConfig {
   private Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
   private Map internalMap;
   private String internalString;

   public JSONConfig(String file) {
      File internalFile = new File(file);
      this.internalString = file;

      try {
         if (!internalFile.exists()) {
            internalFile.createNewFile();
         }

         this.internalMap = JSON.readJSONFromFile(file);
      } catch (Exception var4) {
      }

   }

   public Object get(String path) {
      return this.internalMap.get(path);
   }

   public void set(String path, Object value) {
      this.internalMap.put(path, value);
   }

   public void close() {
      try {
         JSON.writeJSONToFile(this.internalString, this.internalMap);
      } catch (IOException var2) {
      }

   }
}
