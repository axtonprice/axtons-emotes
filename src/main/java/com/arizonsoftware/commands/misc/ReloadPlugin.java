package com.arizonsoftware.commands.misc;

import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.Main;
import com.arizonsoftware.utils.Registry;
import com.arizonsoftware.utils.Strings;

public class ReloadPlugin {

    public ReloadPlugin(CommandSender sender) {

        // Execution validation
        if (!sender.hasPermission("axtonsemotes.reload")) {
            sender.sendMessage(Strings.parse("&4Error: &cYou don't have permission to execute this command!"));
            return;
        }

        // Command start time
        long startTime = System.currentTimeMillis();

        // Reload plugin
        Main instance = Main.getInstance();
        instance.reloadConfig();

        // Re-register commands and listeners
        Main.RegisterAll();
        Registry.resetCounter();

        // Command end time
        long endTime = System.currentTimeMillis();

        // Output message to sender
        sender.sendMessage(
                Strings.parse("&aConfiguration reloaded! (" + String.valueOf(endTime - startTime) + "ms)"));

    }


}