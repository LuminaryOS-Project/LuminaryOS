package com.luminary.os.commands.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.luminary.os.OS;
import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.commands.Command;

public class CDCommand extends Command {
   public CDCommand() {
      super("cd", OS.getLanguage().get("changeDirUsage"), Arrays.asList("cd", "cdir"));
   }

   public void execute(List<String> args) {
      System.out.println(args.size());
      if(args.size() == 0 || Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.println(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()) + "\n");
         return;
      }
      if (!(new File(OS.currentDir + "\\" + args.get(0))).exists()) {
         System.out.println(OS.getLanguage().get("directoryNotFound"));
      } else {
         OS.currentDir = OS.currentDir + "\\" + args.get(0);
      }

   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
