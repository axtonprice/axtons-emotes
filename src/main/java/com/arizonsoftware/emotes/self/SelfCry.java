package com.arizonsoftware.emotes.self;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SelfCry implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Check if sender is not a player
        if (!(sender instanceof Player)){
            sender.sendMessage("§cOnly players can execute this command!");
        }

        // Get command sender player
        assert sender instanceof Player;
        final Player player = (Player) sender;

        // Notify sender
        player.sendMessage("§7§lYou§r§c cry yourself in sorrow..");

        // Send message to all players
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("§7§l" + player.getName() + "§r§7 cries in sorrow..");
        }

        // Emit particles on player
        player.getWorld().spawnParticle(Particle.DRIPPING_WATER, player.getLocation(), 3, 0.5, 0.5, 0.5, 0);

        // Play sound to player
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 1, 1);

        // Output command output
        return true;
    }

}
