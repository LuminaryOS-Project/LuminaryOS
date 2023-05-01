package com.luminary.os.commands;

import java.util.List;

import com.luminary.os.permissions.PermissionLevel;
import org.jetbrains.annotations.NotNull;

public abstract class Command {
   private String name;
   private List<String> aliases;
   public String usage;

   public Command(@NotNull String name, @NotNull String usage, @NotNull List<String> aliases) {
      this.name = name;
      this.usage = usage;
      this.aliases = aliases;
   }

   public String getUsage() {
      return this.usage;
   }

   public String getName() {
      return this.name;
   }

   public List<String> getAliases() {
      return this.aliases;
   }

   public abstract void execute(List<String> args);

   public abstract PermissionLevel getPremission();
}
