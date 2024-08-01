package com.arizonsoftware.commands.misc;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.Main;
import com.arizonsoftware.utils.Strings;

public class ReloadPlugin {

    public ReloadPlugin(@NotNull CommandSender sender) {

        // Command start time
        long startTime = System.currentTimeMillis();

        // Reload plugin
        Main instance = Main.getInstance();
        instance.reloadConfig();

        // Command end time
        long endTime = System.currentTimeMillis();

        // Determine execution time
        long executionTime = endTime - startTime;

        // Output message to sender
        sender.sendMessage(Strings.ParseColors("&aConfiguration reloaded! (" + executionTime + "ms)"));
    }

}