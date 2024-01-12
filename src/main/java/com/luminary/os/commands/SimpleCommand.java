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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class SimpleCommand extends Command {
    private final Consumer<List<String>> cmdfunc;
    private PermissionLevel pL = PermissionLevel.USER;
    public SimpleCommand(@NotNull String name, @NotNull String usage, @NotNull List<String> aliases, Consumer<List<String>> cmdfunc) {
        super(name, usage, aliases);
        this.cmdfunc = cmdfunc;
    }
    public SimpleCommand(@NotNull String name, @NotNull String usage, @NotNull List<String> aliases, @NotNull PermissionLevel permissionLevel, Consumer<List<String>> cmdfunc) {
        super(name, usage, aliases);
        this.pL = permissionLevel;
        this.cmdfunc = cmdfunc;
    }

    @Override
    public void execute(@NotNull List<String> args) {
        cmdfunc.accept(args);
    }

    @Override
    public PermissionLevel getPremission() {
        return pL;
    }
}
