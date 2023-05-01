package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.commands.Command;

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
