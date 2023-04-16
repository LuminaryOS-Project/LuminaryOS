package me.intel.os.commands.impl;

import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;

import java.util.Arrays;
import java.util.Objects;

public class ExitCommand extends Command {
   public ExitCommand() {
      super("exit", "Shuts down the OS", Arrays.asList("ext", "shutdown"));
   }

   public void execute(String[] args) {
      if(args.length != 0 && Objects.equals(args[0], "/?")) {
         System.out.println(this.getUsage());
         System.out.print("Aliases:  " + String.join(",", this.getAliases()) + "\n");
         return;
      }
      System.exit(0);
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
