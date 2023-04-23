package me.intel.os.commands.impl;

import me.intel.os.OS;
import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PWDCommand extends Command {
   public PWDCommand() {
      super("pwd", "pwd / cwd", Arrays.asList("cwd", "pwd"));
   }

   public void execute(List<String> args) {
      System.out.println(OS.getLanguage().get("currentDir") + ": " + OS.currentDir);
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
