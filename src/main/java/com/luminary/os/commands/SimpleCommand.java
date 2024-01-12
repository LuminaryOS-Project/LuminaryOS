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
