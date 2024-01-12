package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.commands.Command;
import com.luminary.os.permissions.PermissionLevel;
import com.luminary.os.utils.Log;
import com.luminary.os.utils.Prompts;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RmCommand extends Command {
   public RmCommand() {
      super("rm", OS.getLanguage().get("rmUsage"), Arrays.asList("remove", "rem"));
   }

   public void execute(List<String> args) {
      if(!args.isEmpty() && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.println(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()));
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
