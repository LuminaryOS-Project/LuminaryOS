package me.intel.os.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Map;

public class JSONConfig {
   private static Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
   @Getter
   private Map internalMap;
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
      return this.internalMap.get(path);
   }

   public void set(String path, Object value) {
      this.internalMap.put(path, value);
   }

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

      try (Reader reader = new FileReader(filename)) {

         return (JSONObject) parser.parse(reader);

      } catch (IOException e) {
         e.printStackTrace();
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
