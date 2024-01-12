package com.luminary.os.plugin;

import com.luminary.os.OS;
import com.luminary.os.utils.JSONConfig;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

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
   OS OS = com.luminary.os.OS.getInstance();
   UUID loadUUID = UUID.randomUUID();

   public abstract void onEnable();

   public abstract void onDisable();

   public String getName() {
      return getClass().getName();
   }
}
