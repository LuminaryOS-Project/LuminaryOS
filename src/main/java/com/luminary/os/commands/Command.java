/*
 * Copyright (c) 2024. Intel
 *
 * This file is part of LuminaryOS
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
