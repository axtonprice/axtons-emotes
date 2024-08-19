package com.arizonsoftware.commands.expressions;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.commands.handlers.Expressions;

public class Cry implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        // Create new shared command
        Expressions emote = Expressions.create(command);

        // Set command details
        emote.setResponses("&7You cry yourself in sorrow..", "&7&l%player%&r&7 cries in a corner..");
        emote.setFX(null, Sound.ENTITY_CAT_HURT);

        // Execute command
        emote.execute(args, command, sender);

        return true;
    }

}
