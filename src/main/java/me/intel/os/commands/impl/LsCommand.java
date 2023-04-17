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

   public void execute(List<String> args) {
      if(args.size() != 0 && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.println("Aliases:  " + String.join(" ", this.getAliases()));
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
      return null;
   }
}
