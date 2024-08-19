package com.arizonsoftware.commands.shared;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.commands.handlers.SharedEmotes;

public class Stomp implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        // Create new shared command
        SharedEmotes emote = SharedEmotes.create(command);

        // Generate random message variant
        String[] possibleActions = { "shin-kicked", "foot-stomped", "knee-bashed", "toe-crushed", "rib-kicked",
                "kicked" };
        String pastTense = possibleActions[(int) (Math.random() * possibleActions.length)];

        // Set command details
        emote.setResponses("&cYou " + pastTense + " &l%target%&r&c!", "&c&l%player%&r &c" + pastTense + " you!");
        emote.setFX(Particle.CRIT, Sound.ENTITY_VILLAGER_HURT);

        // Execute command
        emote.execute(args, command, sender);

        return true;
    }

}
