package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.commands.Command;
import com.luminary.os.permissions.PermissionLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExitCommand extends Command {
   public ExitCommand() {
      super("exit", OS.getLanguage().get("exitUsage"), Arrays.asList("ext", "shutdown"));
   }

   public void execute(List<String> args) {
      if(!args.isEmpty() && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.println(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()));
         return;
      }
      System.exit(0);
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
