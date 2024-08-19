package com.arizonsoftware.commands.shared;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.commands.handlers.SharedEmotes;

public class Stare implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        // Create new shared command
        SharedEmotes emote = SharedEmotes.create(command);

        // Set command details
        emote.setResponses("&7You stare at &l%target%&r&7!", "&7&l%player%&r &7stares at you!");
        emote.setFX(Particle.ANGRY_VILLAGER, Sound.AMBIENT_CAVE);

        // Execute command
        emote.execute(args, command, sender);

        return true;
    }

}
