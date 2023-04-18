package me.intel.os.commands;

import me.intel.os.OS;
import me.intel.os.permissions.PermissionLevel;

import java.util.List;

public class TestPluginCmd {

    public TestPluginCmd() {
        OS.getInstance().getCommandManager().registerCommand(new SimpleCommand("dev", "usage", List.of("develop"), PermissionLevel.ROOT ,(args) -> {
            // execute command code here
        }));
    }
}
