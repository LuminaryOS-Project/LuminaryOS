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

package com.luminary.os;

import com.luminary.os.core.User;
import com.luminary.os.plugin.Plugin;
import com.luminary.os.utils.Utils;
import com.luminary.os.utils.network.Request;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;

public class Start {
   public static final Map<String, Object> OSoptions = new HashMap<>();
   private static OptionSet options;
   public static Object getOption(String option) { return options.valueOf(option); }
   @SneakyThrows
   public static void main(String[] args) {
      /*
      First startup
       */
      File mainf = new File("LuminaryOS");
      if (!mainf.exists() && mainf.mkdirs()) {
         if(new File("LuminaryOS/plugins").mkdirs() && new File("LuminaryOS/disks").mkdirs() && new File("LuminaryOS/natives").mkdirs() && new File("LuminaryOS/users").mkdirs() && new File("LuminaryOS/cache").mkdirs() && new File("LuminaryOS/config").mkdirs() && new File("LuminaryOS/temp").mkdirs() && new File("LuminaryOS/langs").mkdirs()) {
            new File("LuminaryOS/cache/cache.json").createNewFile();
            new File("LuminaryOS/config/config.json").createNewFile();
            System.out.println("Downloading languages...");
            List.of("en.json", "cn.json", "de.json", "fr.json", "hu.json", "in.json", "jp.json", "kr.json", "nl.json", "pl.json", "ro.json", "ru.json").forEach(lang -> {
               System.out.print("Downloading " + lang + "... ");
               try {
                  Request.download("https://raw.githubusercontent.com/LuminaryOS-Project/LuminaryOS/main/LuminaryOS/langs/" + lang, null, "LuminaryOS/langs/" + lang);
                  System.out.print("Done." + "\n");
               } catch (IOException e) {
                  System.out.print("Failed." + "\n");
               }
            });
            if(System.getProperty("os.name").toLowerCase().contains("windows")) {
               System.out.print("Downloading Windows Natives... ");
               try {
                  Request.download("https://raw.githubusercontent.com/LuminaryOS-Project/LuminaryOS/main/LuminaryOS/natives/windows.dll", null, "LuminaryOS/natives/windows.dll");
                  System.out.print("Done." + "\n");
               } catch (IOException e) {
                  System.out.print("Failed." + "\n");
               }
            }
            Utils.createDisk(0);
            System.out.print("Enter a Username: ");
            User.createUser(new Scanner(System.in).nextLine(), true);
         } else {
            System.out.println("Failed to create the needed OS files...");
            Utils.deleteDirectory(new File("LuminaryOS"));
         }
      }
      File pluginFolder = new File("LuminaryOS/plugins");
      //
      File[] files = pluginFolder.listFiles((dir, name) -> name.endsWith(".jar"));
      ArrayList<URL> urls = new ArrayList<>();
      ArrayList<String> classes = new ArrayList<>();

      if (files != null) {
         for (File file : files) {
            try (JarFile jarFile = new JarFile(file)) {
               urls.add(new URL("jar:file:plugins/" + file.getName() + "!/"));
               jarFile.stream()
                       .filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                       .forEach(jarEntry -> classes.add(jarEntry.getName()));
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         try(URLClassLoader pluginLoader = new URLClassLoader(urls.toArray(new URL[0]))) {
            classes.forEach(className -> {
               try {
                  Class<?> clazz = pluginLoader.loadClass(className.replaceAll("/", ".").replace(".class", ""));
                  if (Plugin.class.isAssignableFrom(clazz)) {
                     Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
                     String pluginName = plugin.getName();
                     if (OS.isRegistered(plugin.getClass())) {
                        System.out.println("A plugin by the name " + pluginName + " is already registered!");
                     } else {
                        new Thread(() -> {
                           try {
                              plugin.onEnable();
                              System.out.println("Loaded plugin " + clazz.getCanonicalName() + " successfully");
                              OS.registerPlugin(plugin);
                           } catch (Exception e) {
                              plugin.onDisable();
                              e.printStackTrace();
                           }
                        }, "PLUGINLOAD_" + pluginName).start();
                     }
                  }
               } catch (Exception e) {
                  System.out.println(OS.getLanguage().get("exceptionOccurred"));
                  e.printStackTrace();
               }
            });
         }

      }
      //
      OptionParser parser = new OptionParser();
      parser.allowsUnrecognizedOptions();
      Map<String, String> rarg = new HashMap<>();
      rarg.put("debug", "Enables OS debugging");
      rarg.forEach((k, v) -> parser.accepts(k, v).withOptionalArg());
      options = parser.parse(args);
      OS os = new OS();
      os.Start(args);
   }
}
