package com.arizonsoftware.commands.shared;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.commands.handlers.SharedEmotes;

public class DapUp implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        // Create new shared command
        SharedEmotes emote = SharedEmotes.create(command);

        // Generate random outcome
        int random = (int) (Math.random() * 4);

        // Define result based on random outcome
        String result;
        Sound sound;
        if (random == 1) {
            result = "&a&lGood clap!";
            sound = Sound.ITEM_SHIELD_BLOCK;
        } else if (random == 2) {
            result = "&e&lNear miss!";
            sound = Sound.ITEM_TRIDENT_HIT;
        } else {
            result = "&c&lMiss!";
            sound = Sound.ENTITY_EVOKER_AMBIENT;
        }

        // Set command details with dynamic messages
        emote.setResponses("&bYou try to dap up &l%target%&r&b! " + result,
                "&b&l%player%&r &bdapped you up! " + result);
        emote.setFX(Particle.CRIT, sound);

        // Execute command
        emote.execute(args, command, sender);

        return true;
    }

}
