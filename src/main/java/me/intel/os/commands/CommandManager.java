package me.intel.os.commands;

import me.intel.os.OS;
import me.intel.os.core.Process;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
   public ConcurrentHashMap<String, Command> commands = new ConcurrentHashMap<>();

   public void registerCommand(Command command) {
      this.commands.put(command.getName(), command);
   }

   public String getUsage(String command) {
      return this.commands.get(command) != null ? ((Command)this.commands.get(command)).getUsage() : OS.getLanguage().get("commandNotFound");
   }

   public void executeCommand(String command, List<String> args) {
      Process p  = new Process(new Thread(() -> {
         Command cmd = (Command)this.commands.get(command);
         if (cmd != null) {
            cmd.execute(args);
         } else {
            cmd = this.findCommandByAlias(command);
            if (cmd != null) {
               cmd.execute(args);
            } else {
               System.out.println(OS.getLanguage().get("commandNotFound") + command);
            }
         }
      }), 15000L);
      OS.getProcessManager().add(p);

   }
   public TabCompleter getTabCompleter(Command command) {
      return (TabCompleter) command;
   }

   private Command findCommandByAlias(String alias) {
      Iterator<Command> cmds = this.commands.values().iterator();

      Command cmd;
      List<String> aliases;
      do {
         if (!cmds.hasNext()) {
            return null;
         }

         cmd = (Command)cmds.next();
         aliases = cmd.getAliases();
      } while(!aliases.contains(alias));

      return cmd;
   }
}
