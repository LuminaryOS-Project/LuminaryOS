package me.intel.os.commands;

import java.util.List;
import me.intel.os.permissions.PermissionLevel;

public abstract class Command {
   private String name;
   private List aliases;
   public String usage;

   public Command(String name, String usage, List aliases) {
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

   public abstract void execute(String[] var1);

   public abstract PermissionLevel getPremission();
}
