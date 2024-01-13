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

import com.google.common.reflect.ClassPath;
import com.luminary.annotations.RestrictReflect;
import com.luminary.os.core.LuminarySecurityManager;
import com.luminary.os.core.Native;
import com.luminary.os.core.User;
import com.luminary.os.plugin.Plugin;
import com.luminary.os.utils.FileLogger;
import com.luminary.os.utils.Log;
import com.luminary.os.utils.Utils;
import com.luminary.os.utils.network.Request;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;


public class Start {
   public static final Map<String, Object> OSoptions = new HashMap<>();
   private static OptionSet options;
   public static Object getOption(String option) { return options.valueOf(option); }

   public static boolean isMac() {
      return (System.getProperty("os.name").contains("mac"));
   }

   public static boolean isWindows() {
      return (System.getProperty("os.name").contains("win"));
   }

   public static boolean isUnix() {
      String o = System.getProperty("os.name");
      return (o.contains("nix") || o.contains("nux") || o.indexOf("aix") > 0);
   }

   @SneakyThrows
   public static void main(String[] args) {
      /*
      First startup
       */
      FileLogger.init();
      Log.info("OS Startup @ " + new SimpleDateFormat("hh:mma / E dd/MM/yyyy").format(new Date()));
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
            if(isWindows()) {
               System.out.print("Downloading Windows Natives... ");
               try {
                  Request.download("https://raw.githubusercontent.com/LuminaryOS-Project/LuminaryOS/main/LuminaryOS/natives/windows.dll", null, "LuminaryOS/natives/windows.dll");
                  System.out.print("Done." + "\n");
               } catch (IOException e) {
                  System.out.print("Failed." + "\n");
               }
            }
            if(isUnix()) {
               System.out.print("Downloading Linux Natives... ");
               try {
                  Request.download("https://raw.githubusercontent.com/LuminaryOS-Project/LuminaryOS/main/LuminaryOS/natives/linux.so", null, "LuminaryOS/natives/linux.so");
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

      if(Native.supportsNative()) {
         // Testing
         ClassPath.from(Start.class.getClassLoader())
                 .getAllClasses()
                 .stream()
                 .filter(c -> !isBlacklistedPackage(c.getPackageName()))
                 .map(ClassPath.ClassInfo::getName)
                 .map(Start::loadClassSafely)
                 .filter(Objects::nonNull)
                 .filter(c -> c.isAnnotationPresent(RestrictReflect.class))
                 .forEach(clz -> {
                    RestrictReflect rr = clz.getAnnotation(RestrictReflect.class);
                    LuminarySecurityManager.blockFields(clz, Set.of(rr.fields()));
                    LuminarySecurityManager.blockMethods(clz, Set.of(rr.methods()));
                 });
      }
      //
      File pluginFolder = new File("LuminaryOS/plugins");
      //
      File[] files = pluginFolder.listFiles((dir, name) -> name.endsWith(".jar"));
      ArrayList<URL> urls = new ArrayList<>();
      ArrayList<String> classes = new ArrayList<>();
      if (files != null) {
         for (File file : files) {
            try (JarFile jarFile = new JarFile(file)) {
               urls.add(new URL("jar:file:" + file.getAbsolutePath() + "!/"));
               jarFile.getEntry("plugin.yml");
               jarFile.stream()
                       .filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                       .forEach(jarEntry -> {
                          Log.info(String.format("Found jar class \"%s\" from file \"%s\"", jarEntry.getName(), file.getAbsolutePath()), false);
                          classes.add(jarEntry.getName());
                       });
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         URLClassLoader pluginLoader = URLClassLoader.newInstance(urls.toArray(new URL[0]));
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
                  //System.out.println(OS.getLanguage().get("exceptionOccurred"));
                  e.printStackTrace();
               }
            });

      }
      //
      OptionParser parser = new OptionParser();
      parser.allowsUnrecognizedOptions();
      Map<String, String> rarg = new HashMap<>();
      rarg.put("debug", "Enables OS debugging");
      rarg.forEach((k, v) -> parser.accepts(k, v).withOptionalArg());
      options = parser.parse(args);
      OS.initOS(args);
   }

   private static final List<String> packageBlacklist = List.of(
           "kotlin",
           "com.google",
           "java.lang",
           "javassist",
           "lombok"
   );

   private static Class<?> loadClassSafely(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException | NoClassDefFoundError e) {
         return null;
      }
   }

   private static boolean isBlacklistedPackage(String pkg) {
      return packageBlacklist.stream().anyMatch(pkg::startsWith);
   }
}
