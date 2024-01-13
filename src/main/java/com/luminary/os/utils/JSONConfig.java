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

package com.luminary.os.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luminary.os.utils.adapters.VersionAdapter;
import lombok.Getter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JSONConfig implements AutoCloseable {
   private static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
   @Getter
   private Map<String, Object> internalMap = new HashMap<>();
   private final String internalString;
   private final boolean write;

   public JSONConfig(String file) {
      this.write = true;
      File internalFile = new File(file);
      this.internalString = file;

      try {
         if (!internalFile.exists()) {
            internalFile.createNewFile();
         }
         System.out.println("Loading File " + file + "!");
         this.internalMap = readJSONFromFile(file);
      } catch (Exception ex) {
         FileWriter fw = FileLogger.getWriter();
         if(fw != null)
            ex.printStackTrace(new PrintWriter(fw, true));
      }
   }

   public JSONConfig(InputStream stream) {
      this.internalString = null;
      this.write = false;
      try {
         try (Reader reader = new InputStreamReader(stream)) {
            this.internalMap = gson.fromJson(reader, Map.class);
         }
      } catch (IOException ex) {
         FileWriter fw = FileLogger.getWriter();
         if(fw != null)
            ex.printStackTrace(new PrintWriter(fw, true));
      }

   }
   public void adapt(VersionAdapter adapter) {
      adapter.handle();
   }
   public void clear() { internalMap.clear();}
   public void remove(String key) {
      internalMap.remove(key);
   }
   public Object get(String path) {
      String[] keys = path.split("\\."); // split the path by dot notation
      Object value = this.internalMap;
      for (String key : keys) {
         if (value instanceof Map) {
            value = ((Map<?, ?>) value).get(key);
         } else {
            value = null;
            break;
         }
      }
      return value;
   }

   public <T> T getAs(String path) {
      return (T) get(path);
   }
   //
   public Object get(String path, Object defaultValue) {
      String[] keys = path.split("\\.");
      Object value = this.internalMap;
      for (String key : keys) {
         if (value instanceof Map) {
            value = ((Map<?, ?>) value).get(key);
         } else {
            value = defaultValue;
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
         if(write)
            writeJSONToFile(this.internalString, this.internalMap);
      } catch (IOException ex) {
      }
   }
   public static void writeJSONToFile(String filename, Map<String, Object> data) throws IOException {
      if (!(new File(filename)).exists()) {
         (new File(filename)).createNewFile();
      }

      FileWriter writer = new FileWriter(filename);
      writer.write(gson.toJson(data));
      writer.close();
   }

   public static Map<String, Object> readJSONFromFile(String filename) throws IOException {
      File file = new File(filename);
      if (!file.exists() || file.length() == 0) {
         return new HashMap<>();
      }
      try (Reader reader = new FileReader(filename)) {
         return gson.fromJson(reader, Map.class);
      }
   }

   public static String toString(Map<String, Object> data) {
      return gson.toJson(data);
   }

   public static Map<String, Object> fromJSONString(String json) {
      return (Map<String, Object>) gson.fromJson(json, Map.class);
   }
   public void setPref(String pref, Object value) {
      this.set("preference." + pref, value);
   }
   public void setSetting(String setting, Object value) {
      this.set("setting." + setting, value);
   }
   public void setMisc(String misc, Object value) {
      this.set("misc." + misc, value);
   }
}
