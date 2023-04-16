package me.intel.os.plugin;

import java.io.File;
import java.util.UUID;

import lombok.Getter;
import me.intel.os.OS;
import me.intel.os.utils.JSONConfig;
import org.jetbrains.annotations.NotNull;

public abstract class Plugin {
   @NotNull
   public File getFolder() {
      File f = new File("plugins/" + getName() + "/");
      if(!f.exists()) {
         f.mkdirs();
         return f;
      } else {
         return f;
      }
   }
   //
   @NotNull
   public JSONConfig getConfig() {
      return new JSONConfig(getFolder() + "config.json");
   }
   @Getter
   OS OS = me.intel.os.OS.getInstance();
   UUID loadUUID = UUID.randomUUID();

   public abstract void onEnable();

   abstract void onDisable();

   public String getName() {
      return getClass().getName();
   }
}
