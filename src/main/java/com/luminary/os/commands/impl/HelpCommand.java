package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.commands.Command;
import com.luminary.os.permissions.PermissionLevel;

import java.util.List;
import java.util.Objects;

public class HelpCommand extends Command {
   public HelpCommand() {
      super("help", OS.getLanguage().get("helpUsage"), List.of("Help!"));
   }

   public void execute(List<String> args) {
      if(!args.isEmpty() && Objects.equals(args.get(0), "/?")) {
         System.out.println("\n");
         System.out.println(this.getUsage());
         System.out.println(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()));
      } else if(args.isEmpty()){
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
