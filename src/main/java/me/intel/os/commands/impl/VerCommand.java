package me.intel.os.commands.impl;

import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VerCommand extends Command {
   public VerCommand() {
      super("ver", "ver / version", Arrays.asList("ver", "version"));
   }

   public void execute(List<String> args) {
      System.out.println("---------------------------");
      System.out.println("IntelOS Beta 1.0-R1");
      System.out.println("Running on " + System.getProperty("os.name"));
      System.out.println("---------------------------");
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
