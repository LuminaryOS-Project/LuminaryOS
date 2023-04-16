package me.intel.os.commands;

import me.intel.os.OS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CommandManager {
   public HashMap<String, Command> commands = new HashMap<>();

   public void registerCommand(Command command) {
      this.commands.put(command.getName(), command);
   }

   public String getUsage(String command) {
      return this.commands.get(command) != null ? ((Command)this.commands.get(command)).getUsage() : "Unknown Command";
   }

   public void executeCommand(String command, String[] args) {
      Thread t = new Thread(() -> {
         Command cmd = (Command)this.commands.get(command);
         if (cmd != null) {
            cmd.execute(args);
         } else {
            cmd = this.findCommandByAlias(command);
            if (cmd != null) {
               cmd.execute(args);
            } else {
               System.out.println("Command not found: " + command);
            }
         }
      });
      t.start();
      OS.getProcessManager().add(t, 15000);

   }
   public TabCompleter getTabCompleter(Command command) {
      return (TabCompleter) command;
   }

   private Command findCommandByAlias(String alias) {
      Iterator var2 = this.commands.values().iterator();

      Command cmd;
      List aliases;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         cmd = (Command)var2.next();
         aliases = cmd.getAliases();
      } while(!aliases.contains(alias));

      return cmd;
   }
}
