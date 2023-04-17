package me.intel.os.commands.impl;

import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExitCommand extends Command {
   public ExitCommand() {
      super("exit", "Shuts down the OS", Arrays.asList("ext", "shutdown"));
   }

   public void execute(List<String> args) {
      if(args.size() != 0 && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.print("Aliases:  " + String.join(" ", this.getAliases()));
         return;
      }
      System.exit(0);
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
