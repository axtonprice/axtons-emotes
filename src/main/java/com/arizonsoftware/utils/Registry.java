package com.arizonsoftware.utils;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class Registry {

    public static int commandCount = 0;

    public static void registerCommand(PluginCommand command, Object executor) {
        // Check if provided executor is type TabExecutor or CommandExecutor
        if (!(executor instanceof TabCompleter) && !(executor instanceof CommandExecutor)) {
            throw new IllegalArgumentException("Executor must implement TabCompleter or CommandExecutor");
        }
        // Set executor and tab completer (if executor is of type tab completer)
        if (command != null) {
            command.setExecutor((CommandExecutor) executor);
            if ((executor instanceof TabCompleter))
                command.setTabCompleter((TabCompleter) executor);
        }
        // Append 1 to command count total
        commandCount++;
    }
}
