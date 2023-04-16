package me.intel.os.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class JSON {
   private static Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

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
