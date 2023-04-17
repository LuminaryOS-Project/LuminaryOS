package me.intel.os.commands.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import me.intel.os.OS;
import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;

public class CDCommand extends Command {
   public CDCommand() {
      super("cd", "Changes your current directory", Arrays.asList("cd", "cdir"));
   }

   public void execute(List<String> args) {
      if(args.size() != 0 && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.print("Aliases:  " + String.join(" ", this.getAliases()));
         return;
      }
      if (!(new File(OS.currentDir + "\\" + args.get(0))).exists()) {
         System.out.println("Directory does not exist!");
      } else {
         OS.currentDir = OS.currentDir + "\\" + args.get(0);
      }

   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
