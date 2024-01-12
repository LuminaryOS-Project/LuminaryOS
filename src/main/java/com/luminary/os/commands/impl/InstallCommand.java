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
