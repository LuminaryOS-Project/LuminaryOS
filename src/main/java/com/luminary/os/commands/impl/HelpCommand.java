package com.luminary.os.commands.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.luminary.os.OS;
import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.commands.Command;

public class HelpCommand extends Command {
   public HelpCommand() {
      super("help", OS.getLanguage().get("helpUsage"), Arrays.asList("Help!"));
   }

   public void execute(List<String> args) {
      if(args.size() != 0 && Objects.equals(args.get(0), "/?")) {
         System.out.println("\n");
         System.out.println(this.getUsage());
         System.out.println(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()));
         return;
      } else if(args.size() == 0){
         OS.getInstance().getCommandManager().commands.forEach((k,v) -> {
            System.out.println("\n");
            System.out.println(v.getName() + " - " + v.getUsage());
            System.out.println(OS.getLanguage().get("aliases") + ": " + String.join(" ",v.getAliases()));
         });
      }
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
