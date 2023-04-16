package me.intel.os.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface TabCompleter {
    default List<String> getCompletions(String[] args, int position) {
        return Collections.emptyList();
    };
}
