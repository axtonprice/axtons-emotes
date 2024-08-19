package com.arizonsoftware.commands.misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.utils.Strings;
import com.arizonsoftware.utils.Validation;

public class ListExpressions implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        // Execution validation
        if (!Validation.checkIsEnabled(command, sender))
            return true;
        if (!Validation.checkIsPlayer(sender))
            return true;
        if (!Validation.checkHasPermission(sender, command))
            return true;

        // Execute command
        execute(sender, args);

        // Output command output
        return true;
    }

    public void execute(CommandSender sender, @NotNull String[] args) {
        sender.sendMessage(Strings.parse("&7Coming soon!"));
    }

}
