package me.intel.os.commands.impl;

import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UnameCommand extends Command {
   public UnameCommand() {
      super("uname", "uname -m ", Arrays.asList("uname"));
   }

   public void execute(List<String> args) {
      if(args.size() == 0 || Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.println("Aliases:  " + String.join(" ", this.getAliases()));
         return;
      } else {
         switch (args.get(0)) {
            case "-m":
               System.out.println("OS: " + System.getProperty("os.name"));
         }
      }
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
