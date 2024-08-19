package com.arizonsoftware.commands.shared;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.commands.handlers.SharedEmotes;

public class Applaud implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        // Create new shared command
        SharedEmotes emote = SharedEmotes.create(command);

        // Set command details
        emote.setResponses("&eYou applauded &l%target%&r&e!", "&e&l%player%&r &eapplauded you!");
        emote.setFX(Particle.HAPPY_VILLAGER, Sound.ENTITY_VILLAGER_CELEBRATE);

        // Execute command
        emote.execute(args, command, sender);

        return true;
    }

}
