package me.intel.os.plugin;

import java.util.UUID;
import me.intel.os.OS;

public interface Plugin {
   OS OS = me.intel.os.OS.getInstance();
   UUID loadUUID = UUID.randomUUID();

   boolean onEnable();

   void onDisable();

   String getName();

   String getVersion();
}
