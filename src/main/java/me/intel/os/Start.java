package me.intel.os;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import me.intel.os.plugin.Plugin;
import me.intel.os.utils.Utils;

public class Start {
   public static Map<String, Object> OSoptions = new HashMap<>();
   public static void main(String[] args) throws InterruptedException, IOException {
      long startNS = System.nanoTime();
      // Plugins
      File pluginFolder = new File("plugins");
      if (!pluginFolder.exists() && pluginFolder.mkdirs()) {
         System.out.println("Created plugin folder");
      }
      /*
      First startup

       */
      File mainf = new File("IntelOS");
      if (!mainf.exists() && mainf.mkdirs()) {
         if(new File("IntelOS/disks").mkdirs() && new File("IntelOS/users").mkdirs() && new File("IntelOS/cache").mkdirs() && new File("IntelOS/config").mkdirs() && new File("IntelOS/temp").mkdirs() && new File("IntelOS/langs").mkdirs()) {
            new File("IntelOS/cache/cache.json").createNewFile();
            new File("IntelOS/config/config.json").createNewFile();
            Utils.createDisk(0);
         } else {
            System.out.println("Failed to create the needed OS files...");
            Utils.deleteDirectory(new File("IntelOS"));
         }
      }
      //
      File[] files = pluginFolder.listFiles((dir, name) -> name.endsWith(".jar"));
      ArrayList urls = new ArrayList();
      ArrayList<String> classes = new ArrayList();
      if (files != null) {
         Arrays.stream(files).forEach(file -> {
            try {
               JarFile jarFile = new JarFile(file);
               urls.add(new URL("jar:file:plugins/" + file.getName() + "!/"));
               jarFile.stream().filter(jarEntry -> jarEntry.getName().endsWith(".class")).forEach(jarEntry -> classes.add(jarEntry.getName()));
            } catch (IOException e) { e.printStackTrace(); }
         });

         URLClassLoader pluginLoader = new URLClassLoader((URL[])urls.toArray(new URL[urls.size()]));
         classes.forEach((s) -> {
            try {
               Class<?> classs = pluginLoader.loadClass(s.replaceAll("/", ".").replace(".class", ""));
               Class<?>[] interfaces = classs.getInterfaces();
               if (Plugin.class.isAssignableFrom(classs)) {
                  Plugin plugin = (Plugin)classs.newInstance();
                  try {
                     if (OS.nameToPlugin.containsKey(plugin.getName())) {
                        System.out.println("A plugin by the name %name% is already registered!".replace("%name%", plugin.getName()));
                     } try {
                        new Thread(() -> {
                           plugin.onEnable();
                           System.out.println("Loaded plugin " + classs.getCanonicalName() + " successfully");
                           OS.nameToPlugin.put(plugin.getName(), plugin);
                        }, "PLUGINLOAD_" + plugin.getName()).start();

                     } catch (Exception e) {
                        e.printStackTrace();
                     }

                  } catch (Exception e) {
                     System.out.println(OS.getLanguage().get("errorOccurred"));
                     e.printStackTrace();
                     plugin.onDisable();
                  }
               }
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException var11) {
               System.out.println(OS.getLanguage().get("exceptionOccurred"));
               var11.printStackTrace();
            }

         });
      }
      //
      OptionParser parser = new OptionParser();
      parser.accepts("debug", "Enables OS debugging")
              .withOptionalArg();

      OptionSet options = parser.parse(args);
      if(options.has("debug")) {
         OSoptions.put("debug", true);
      }
      //
      OS os = new OS();
      System.out.println("Finished booting.");
      System.out.println("Completed in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNS) + "ms");
      os.Start(args);
   }
}
