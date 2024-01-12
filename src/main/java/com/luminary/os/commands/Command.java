package com.luminary.os.commands;

import com.luminary.os.permissions.PermissionLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class Command {
   public static final PermissionLevel DEFAULT_PERMISSION = PermissionLevel.USER;
   private final String name;
   private final List<String> aliases;
   public final String usage;

   public Command(@NotNull String name, @NotNull String usage, @NotNull List<String> aliases) {
      this.name = name;
      this.usage = usage;
      this.aliases = aliases;
   }

   public abstract void execute(List<String> args);

   public abstract PermissionLevel getPremission();
}
