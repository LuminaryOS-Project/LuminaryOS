package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.commands.Command;
import com.luminary.os.permissions.PermissionLevel;

import java.util.Arrays;
import java.util.List;

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
