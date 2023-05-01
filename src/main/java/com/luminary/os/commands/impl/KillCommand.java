package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.commands.Command;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class KillCommand extends Command {
   public KillCommand() {
      super("kill", OS.getLanguage().get("killUsage"), Arrays.asList("kill", "taskkill"));
   }

   public void execute(List<String> args) {
      if(args.size() == 0 || Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.print(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()) + "\n");
         return;
      }
      try {
         int id = Integer.parseInt(args.get(0));
         System.out.println("Attempted to kill process with id  " + id);
         OS.getProcessManager().kill(id);
      } catch (Exception e) {
         System.out.println(this.getUsage());
         System.out.print(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()) + "\n");
         e.printStackTrace();
         return;
      }
   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
