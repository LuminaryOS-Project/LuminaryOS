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

package com.luminary.os.commands.impl;

import com.luminary.os.commands.Command;
import com.luminary.os.permissions.PermissionLevel;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.util.List;

public class InstallCommand extends Command {
    public InstallCommand() {
        super("install", "Installs a package from online.", List.of("apt", "install"));
    }

    @Override
    public void execute(List<String> args) {
        OptionParser parser = new OptionParser();
        boolean disableHC = args.contains("-dhc");
        if(args.size() > 1 && args.get(0).equalsIgnoreCase("-u")) {
            // update cahce
        }

        parser.allowsUnrecognizedOptions();
        parser.accepts("r", "Selects a custom repository (Not Supported).").withOptionalArg();
        parser.accepts("v", "Selects version of the package to install if found.").withOptionalArg();
        parser.accepts("ua", "Updates a specified app if able to.");
        OptionSet options = parser.parse(args.toArray(new String[0]));

    }

    @Override
    public PermissionLevel getPremission() {
        return DEFAULT_PERMISSION;
    }
}
