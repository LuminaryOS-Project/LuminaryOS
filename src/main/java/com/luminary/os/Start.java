package com.luminary.os;

import com.luminary.os.core.User;
import com.luminary.os.plugin.Plugin;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.jar.JarFile;

public class Start {
   public static Map<String, Object> OSoptions = new HashMap<>();
   private static OptionSet options;
   public static Object getOption(String option) { return options.valueOf(option); }
   @SneakyThrows
   public static void main(String[] args) throws IOException {
      //long startNS = System.nanoTime();
      // Plugins
      File pluginFolder = new File("plugins");
      if (!pluginFolder.exists() && pluginFolder.mkdirs()) {
         System.out.println("Created plugin folder");
      }
      /*
      First startup

       */
      File mainf = new File("LuminaryOS");
      if (!mainf.exists() && mainf.mkdirs()) {
         if(new File("LuminaryOS/disks").mkdirs() && new File("LuminaryOS/users").mkdirs() && new File("LuminaryOS/cache").mkdirs() && new File("LuminaryOS/config").mkdirs() && new File("LuminaryOS/temp").mkdirs() && new File("LuminaryOS/langs").mkdirs()) {
            new File("LuminaryOS/cache/cache.json").createNewFile();
            new File("LuminaryOS/config/config.json").createNewFile();
            Utils.createDisk(0);
            System.out.print("Input your future username: ");
            User.createUser(new Scanner(System.in).nextLine(), true);
            Log.warn("Without a working internet connection the setup can't continue please make sure you have a working internet connection");
            DlReqDeps();
         } else {
            System.out.println("Failed to create the needed OS files...");
            Utils.deleteDirectory(new File("LuminaryOS"));
         }
      }
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

         URLClassLoader pluginLoader = new URLClassLoader(urls.toArray(new URL[0]));
         classes.forEach(className -> {
            try {
               Class<?> clazz = pluginLoader.loadClass(className.replaceAll("/", ".").replace(".class", ""));
               if (Plugin.class.isAssignableFrom(clazz)) {
                  Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
                  String pluginName = plugin.getName();
                  if (OS.nameToPlugin.containsKey(pluginName)) {
                     System.out.println("A plugin by the name " + pluginName + " is already registered!");
                  } else {
                     new Thread(() -> {
                        try {
                           plugin.onEnable();
                           System.out.println("Loaded plugin " + clazz.getCanonicalName() + " successfully");
                           OS.nameToPlugin.put(pluginName, plugin);
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
      //
      OptionParser parser = new OptionParser();
      Map<String, String> rarg = new HashMap<>();
      rarg.put("debug", "Enables OS debugging");
      rarg.put("val", "idk");
      rarg.forEach((k, v) -> {
         parser.accepts(k, v).withOptionalArg();
      });
      options = parser.parse(args);
      OS os = new OS();
      Log.info("Hello");

      os.Start(args);
   }

   private static void DlReqDeps() throws Exception {

      Request.download("https://raw.githubusercontent.com/LuminaryOS-Project/luminaryos-project.github.io/main/data/langs/en.json", null, "LuminaryOS/langs/en.json");
   }
}
