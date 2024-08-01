package com.arizonsoftware.commands.expressions;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.utils.Strings;
import com.arizonsoftware.utils.Validation;

public class Sulk implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        // Execution validation
        if (!Validation.checkHasPermission(sender, getClass()))
            return true;
        if (!Validation.checkIsPlayer(sender))
            return true;

        // Get player from sender
        Player player = (Player) sender;

        // Notify sender
        player.sendMessage(Strings.ParseColors("&7You turn your head and pout.."));

        // Send message to all players except sender
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player)
                p.sendMessage(Strings.ParseColors("&7&l" + player.getName() + "&r&7 sulks in a corner."));
        }

        // Emit particles on player
        player.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, player.getLocation(), 10, 0.5, 1, 0.5, 0);

        // Play sound to player
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_ATTACK, 1, 1);

        // Output command output
        return true;
    }

}
