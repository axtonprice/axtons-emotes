package com.arizonsoftware.commands.expressions;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.commands.handlers.Expressions;

public class Wave implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        // Create new shared command
        Expressions emote = Expressions.create(command);

        // Set command details
        emote.setResponses("&bYou wave to everyone!", "&b&l%player%&r&b waves to everyone!");
        emote.setFX(null, Sound.ENTITY_VILLAGER_CELEBRATE);

        // Execute command
        emote.execute(args, command, sender);

        return true;
    }

}
