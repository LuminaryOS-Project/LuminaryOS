package com.luminary.os.commands.impl;

import com.luminary.os.OS;
import com.luminary.os.commands.Command;
import com.luminary.os.commands.TabCompleter;
import com.luminary.os.permissions.PermissionLevel;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LsCommand extends Command implements TabCompleter {
   public LsCommand() {
      super("ls", OS.getLanguage().get("lsUsage"), Arrays.asList("dir", "listdir"));
   }

   public void execute(List<String> args) {
      if(!args.isEmpty() && Objects.equals(args.get(0), "/?")) {
         System.out.println(this.getUsage());
         System.out.println(OS.getLanguage().get("aliases") + ": " + String.join(" ", this.getAliases()));
         return;
      }
      File folder = new File(OS.currentDir);
      File[] listOfFiles = folder.listFiles();

       if (listOfFiles != null) {
           for(int i = 0; i < listOfFiles.length; ++i) {
              if (listOfFiles[i].isFile()) {
                 System.out.println(OS.getLanguage().get("file") + ": " + listOfFiles[i].getName());
              } else if (listOfFiles[i].isDirectory()) {
                 System.out.println(OS.getLanguage().get("directory") + ": " + listOfFiles[i].getName());
              }
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
