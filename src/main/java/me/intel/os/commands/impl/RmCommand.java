package me.intel.os.commands.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import me.intel.os.commands.Command;
import me.intel.os.permissions.PermissionLevel;
import me.intel.os.utils.Log;
import me.intel.os.utils.Prompts;
import me.intel.os.utils.Utils;

public class RmCommand extends Command {
   public RmCommand() {
      super("rm", "Removes a file", Arrays.asList("remove", "rem"));
   }

   public void execute(List<String> args) {
      if(args.size() != 0 && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.println("Aliases:  " + String.join(" ", this.getAliases()));
         return;
      }
      File f = new File(args.get(0));
      if (!f.exists()) {
         Log.info("[!] File does not exist");
      } else {
         if(Prompts.YNPrompt()) { f.delete(); } else { return; }
         Log.info("[-] File removed.");
      }

   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }
}
