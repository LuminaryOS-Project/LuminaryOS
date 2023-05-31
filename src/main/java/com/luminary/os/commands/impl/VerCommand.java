package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.core.Native;
import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.commands.Command;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class VerCommand extends Command {
   public VerCommand() {
      super("ver", "ver / version", Arrays.asList("ver", "version"));
   }

   public void execute(List<String> args) {


      System.out.println("---------------------------");
      System.out.println("LuminaryOS: " + OS.getLanguage().get("version") + " " + OS.VERSION + " (Build: " + OS.BUILD_NUM + ")");
      if(Native.supportsNative() && OS.getNATIVE().isPresent()) {
         System.out.println("Native: " + OS.getNATIVE().get().getInfo());
      }
      System.out.println("Running on: " + System.getProperty("os.name"));
      System.out.println("Language Pack: " + OS.getLanguage().getName() + " designed for: " + OS.getLanguage().getVersion());
      System.out.println("---------------------------");
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
