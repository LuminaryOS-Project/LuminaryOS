package me.intel.os.commands.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import me.intel.os.OS;
import me.intel.os.commands.Command;
import me.intel.os.commands.CommandManager;
import me.intel.os.permissions.PermissionLevel;

public class HelpCommand extends Command {
   public HelpCommand() {
      super("help", "Shows the valid syntax for a command", Arrays.asList("Help!"));
   }

   public void execute(List<String> args) {
      if(args.size() != 0 && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.println("Aliases:  " + String.join(" ", this.getAliases()));
         return;
      } else if(args.size() == 0){
         OS.getInstance().getCommandManager().commands.forEach((k,v) -> {
            System.out.println(v.getName() + " - " + v.getUsage());
            System.out.println("Aliases: " + String.join(" ",v.getAliases()));
            System.out.println("\n");
         });
      }
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
