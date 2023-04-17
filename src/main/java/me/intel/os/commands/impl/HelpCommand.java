package me.intel.os.commands.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import me.intel.os.OS;
import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;

public class HelpCommand extends Command {
   public HelpCommand() {
      super("help", "Shows the valid syntax for a command", Arrays.asList("Help!"));
   }

   public void execute(List<String> args) {
      if(args.size() != 0 && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.print("Aliases:  " + String.join(" ", this.getAliases()));
         return;
      }
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
