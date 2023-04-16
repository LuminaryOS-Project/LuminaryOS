package me.intel.os.commands.impl;

import java.util.Arrays;
import me.intel.os.OS;
import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;

public class HelpCommand extends Command {
   public HelpCommand() {
      super("help", "Shows the valid syntax for a command", Arrays.asList("Help!"));
   }

   public void execute(String[] args) {
      if (args.length != 0) {
         String usage = OS.getInstance().getCommandManager().getUsage(args[0]);
         if (usage != null) {
            System.out.println(usage);
         }
      }

   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
