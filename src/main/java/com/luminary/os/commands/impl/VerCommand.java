package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.commands.Command;

import java.util.Arrays;
import java.util.List;

public class VerCommand extends Command {
   public VerCommand() {
      super("ver", "ver / version", Arrays.asList("ver", "version"));
   }

   public void execute(List<String> args) {
      System.out.println("---------------------------");
      System.out.println("LuminaryOS " + OS.getLanguage().get("version") + " " + OS.getVersion());
      System.out.println("Running on " + System.getProperty("os.name"));
      System.out.println("Language Pack: " + OS.getLanguage().getName() + " designed for: " + OS.getLanguage().getVersion());
      System.out.println("---------------------------");
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
