package me.intel.os.commands.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import me.intel.os.OS;
import me.intel.os.commands.Command;
import me.intel.os.commands.TabCompleter;
import me.intel.os.permissions.PermissionLevel;

public class LsCommand extends Command implements TabCompleter {
   public LsCommand() {
      super("ls", "Lists all files in a directory", Arrays.asList("dir", "listdir"));
   }

   public void execute(String[] args) {
      if(args.length != 0 && Objects.equals(args[0], "/?")) {
         System.out.println(this.getUsage());
         System.out.print("Aliases:  " + String.join(" ", this.getAliases()) + "\n");
         return;
      }
      File folder = new File(OS.currentDir);
      File[] listOfFiles = folder.listFiles();

      for(int i = 0; i < listOfFiles.length; ++i) {
         if (listOfFiles[i].isFile()) {
            System.out.println("File: " + listOfFiles[i].getName());
         } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory: " + listOfFiles[i].getName());
         }
      }

   }

   public PermissionLevel getPremission() {
      return PermissionLevel.USER;
   }

   @Override
   public List<String> getCompletions(String[] args, int position) {
      if (position == 0) {
         // Return completions for the first argument position
         return Arrays.asList("arg1", "arg2", "arg3");
      } else if (position == 1) {
         // Return completions for the second argument position
         return Arrays.asList("value1", "value2", "value3");
      } else {
         // No completions for other argument positions
         return Collections.emptyList();
      }
   }
}
