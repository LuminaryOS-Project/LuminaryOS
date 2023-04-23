package me.intel.os.commands.impl;

import me.intel.os.OS;
import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DummyCommand extends Command {
   public DummyCommand() {
      super("dummy", "dummy", Arrays.asList("dum", "my"));
   }

   public void execute(List<String> args) {
      if(args.size() != 0 && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.print(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()) + "\n");
         return;
      }
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
