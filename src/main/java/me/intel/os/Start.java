package me.intel.os;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import me.intel.os.plugin.Plugin;

public class Start {
   static final String PLUGIN_FOLDER = "plugins";

   public static void main(String[] args) throws InterruptedException, IOException {
      System.out.println("Starting...");
      long startNS = System.nanoTime();
      File pluginFolder = new File("plugins");
      if (!pluginFolder.exists() && pluginFolder.mkdirs()) {
         System.out.println("Created plugin folder");
      }

      File[] files = pluginFolder.listFiles((dir, name) -> {
         return name.endsWith(".jar");
      });
      ArrayList urls = new ArrayList();
      ArrayList<String> classes = new ArrayList();
      if (files != null) {
         Arrays.stream(files).forEach((file) -> {
            try {
               JarFile jarFile = new JarFile(file);
               urls.add(new URL("jar:file:plugins/" + file.getName() + "!/"));
               jarFile.stream().forEach((jarEntry) -> {
                  if (jarEntry.getName().endsWith(".class")) {
                     classes.add(jarEntry.getName());
                  }

               });
            } catch (IOException var4) {
               var4.printStackTrace();
            }

         });
         URLClassLoader pluginLoader = new URLClassLoader((URL[])urls.toArray(new URL[urls.size()]));
         classes.forEach((s) -> {
            try {
               Class classs = pluginLoader.loadClass(s.replaceAll("/", ".").replace(".class", ""));
               Class[] interfaces = classs.getInterfaces();
               Class[] var4 = interfaces;
               int var5 = interfaces.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  Class anInterface = var4[var6];
                  if (anInterface == Plugin.class) {
                     Plugin plugin = (Plugin)classs.newInstance();

                     try {
                        if (OS.nameToPlugin.containsKey(plugin.getName())) {
                           System.out.println("A plugin by the name %name% is already registered!".replace("%name%", plugin.getName()));
                        } else if (plugin.onEnable()) {
                           System.out.println("Loaded plugin " + classs.getCanonicalName() + " successfully");
                           OS.nameToPlugin.put(plugin.getName(), plugin);
                        }
                        break;
                     } catch (Exception var10) {
                        System.out.println("Error occurred while enabling plugin!");
                        var10.printStackTrace();
                        plugin.onDisable();
                     }
                  }
               }
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException var11) {
               System.out.println("Exception occurred during instancing...");
               var11.printStackTrace();
            }

         });
      }

      OS os = new OS();
      System.out.println("Finished booting.");
      System.out.println("Completed in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNS) + "ms");
      os.Start(args);
   }
}
