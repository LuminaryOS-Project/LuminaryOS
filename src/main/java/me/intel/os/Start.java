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
      File[] files = pluginFolder.listFiles((dir, name) -> name.endsWith(".jar"));
      List<URL> urls = new ArrayList<>();
      List<String> classes = new ArrayList<>();
      if (files != null) {
         Arrays.stream(files).forEach(file -> {
            try {
               JarFile jarFile = new JarFile(file);
               urls.add(new URL("jar:file:plugins/" + file.getName() + "!/"));
               jarFile.stream().filter(jarEntry -> jarEntry.getName().endsWith(".class")).forEach(jarEntry -> classes.add(jarEntry.getName()));
            } catch (IOException e) { e.printStackTrace(); }
         });
         URLClassLoader pluginLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
         classes.forEach(className -> {
            try {
               Class<?> clazz = pluginLoader.loadClass(className.replaceAll("/", ".").replace(".class", ""));
               Class<?>[] interfaces = clazz.getInterfaces();
               for (Class<?> anInterface : interfaces) {
                  if (anInterface == Plugin.class) {
                     Plugin plugin = (Plugin) clazz.newInstance();
                     if (OS.nameToPlugin.containsKey(plugin.getName())) {
                        System.out.println("A plugin by the name %name% is already registered!".replace("%name%", plugin.getName()));
                     } else {
                        try {
                           plugin.onEnable();
                           System.out.println("Loaded plugin " + clazz.getCanonicalName() + " successfully");
                           OS.nameToPlugin.put(plugin.getName(), plugin);
                        } catch (Exception err) {
                           System.out.println("Failed to load plugin");
                           err.printStackTrace();
                        }
                     }
                     break;
                  }
               }
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
               System.out.println("Exception occurred during instancing...");
               e.printStackTrace();
            }
         });
      }

      OS os = new OS();
      System.out.println("Finished booting.");
      System.out.println("Completed in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNS) + "ms");
      os.Start(args);
   }
}
