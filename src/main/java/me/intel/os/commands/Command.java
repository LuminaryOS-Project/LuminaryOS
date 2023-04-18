package me.intel.os.commands;

import java.util.List;
import java.util.function.Consumer;

import me.intel.os.permissions.PermissionLevel;
import static org.jetbrains.annotations.ApiStatus.Experimental;
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

   public List getAliases() {
      return this.aliases;
   }

   public abstract void execute(List<String> args);

   public abstract PermissionLevel getPremission();
}
