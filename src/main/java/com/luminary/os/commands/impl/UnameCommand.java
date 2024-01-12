package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.commands.Command;
import com.luminary.os.permissions.PermissionLevel;

import java.util.List;
import java.util.Objects;

public class UnameCommand extends Command {
   public UnameCommand() {
      super("uname", "uname -m ", List.of("uname"));
   }

   public void execute(List<String> args) {
      if(args.isEmpty() || Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.println(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()));
      } else {
          if (args.get(0).equals("-m")) {
              System.out.println("OS: " + System.getProperty("os.name"));
          }
      }
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
