package com.luminary.os.commands;

import com.luminary.os.OS;
import com.luminary.os.core.Process;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
   public final ConcurrentHashMap<String, Command> commands = new ConcurrentHashMap<>();
   private static CommandManager cmdmgr;
   public void registerCommand(Command command) {
      this.commands.put(command.getName(), command);
   }
   private CommandManager() {}
   public static CommandManager getInstance() {
      if(cmdmgr == null) {
         cmdmgr = new CommandManager();
      }
      return cmdmgr;
   }
   public String getUsage(String command) {
      return this.commands.get(command) != null ? this.commands.get(command).getUsage() : OS.getLanguage().get("commandNotFound");
   }

   public void executeCommand(String command, List<String> args) {
      Process p  = new Process(new Thread(() -> {
         Command cmd = this.commands.get(command);
         if (cmd != null) {
            cmd.execute(args);
         } else {
            cmd = this.findCommandByAlias(command);
            if (cmd != null) {
               cmd.execute(args);
            } else {
               System.out.println(OS.getLanguage().get("commandNotFound") + " " + command);
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

         cmd = cmds.next();
         aliases = cmd.getAliases();
      } while(!aliases.contains(alias));

      return cmd;
   }
}
