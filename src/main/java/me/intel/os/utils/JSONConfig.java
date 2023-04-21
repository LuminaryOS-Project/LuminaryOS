package me.intel.os.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JSONConfig implements AutoCloseable {
   private static Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
   @Getter
   private Map<String, Object> internalMap = new HashMap<>();
   private String internalString;

   public JSONConfig(String file) {
      File internalFile = new File(file);
      this.internalString = file;

      try {
         if (!internalFile.exists()) {
            internalFile.createNewFile();
         }

         this.internalMap = readJSONFromFile(file);
      } catch (Exception var4) {
      }

   }

   public Object get(String path) {
      String[] keys = path.split("\\."); // split the path by dot notation
      Object value = this.internalMap;
      for (String key : keys) {
         if (value instanceof Map) {
            value = ((Map) value).get(key);
         } else {
            value = null;
            break;
         }
      }
      return value;
   }

   public void set(String path, Object value) {
      String[] keys = path.split("\\.");
      Map<String, Object> map = this.internalMap;
      for (int i = 0; i < keys.length - 1; i++) {
         if (!map.containsKey(keys[i])) {
            map.put(keys[i], new HashMap<>());
         }
         map = (Map<String, Object>) map.get(keys[i]);
      }
      map.put(keys[keys.length - 1], value);
   }
   @Override
   public void close() {
      try {
         writeJSONToFile(this.internalString, this.internalMap);
      } catch (IOException var2) {
      }
   }
   public static void writeJSONToFile(String filename, Map data) throws IOException {
      if (!(new File(filename)).exists()) {
         (new File(filename)).createNewFile();
      }

      FileWriter writer = new FileWriter(filename);
      writer.write(gson.toJson(data));
      writer.close();
   }

   public static JSONObject readJSONFromFile(String filename) throws IOException {
      JSONParser parser = new JSONParser();
      File file = new File(filename);
      if (!file.exists() || file.length() == 0) {
         return new JSONObject();
      }
      try (Reader reader = new FileReader(filename)) {
         return (JSONObject) parser.parse(reader);
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return null;
   }

   public static String toString(Map data) {
      return gson.toJson(data);
   }

   public static Map fromJSONString(String json) {
      TypeToken token = new TypeToken() {
      };
      return (Map)gson.fromJson(json, token.getType());
   }
}
