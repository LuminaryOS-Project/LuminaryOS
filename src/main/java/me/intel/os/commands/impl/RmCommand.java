package me.intel.os.commands.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;
import me.intel.os.utils.Prompts;
import me.intel.os.utils.Utils;

public class RmCommand extends Command {
   public RmCommand() {
      super("rm", "Removes a file", Arrays.asList("remove", "rem"));
   }

   public void execute(String[] args) {
      if(args.length != 0 && Objects.equals(args[0], "/?")) {
         System.out.println(this.getUsage());
         System.out.print("Aliases:  " + String.join(" ", this.getAliases()) + "\n");
         return;
      }
      File f = new File(args[0]);
      if (!f.exists()) {
         Utils.printerr("File does not exist");
      } else {
         if(Prompts.YNPrompt()) { f.delete(); } else { return; }
         Utils.println("[-] File removed.");
      }

   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
