package com.arizonsoftware.emotes.shared;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getServer;

public class Poke implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Check if sender is not a player
        if (!(sender instanceof Player)){
            sender.sendMessage("§cOnly players can execute this command!");
        }

        // Check if targeted player is not online
        if (getServer().getPlayer(args[0]) == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }

        // Check if sender targeted themselves
        if (args[0].equals(sender.getName())) {
            sender.sendMessage("§cYou can't execute this command on yourself!");
            return true;
        }

        // Get command sender player
        assert sender instanceof Player;
        final Player player = (Player) sender;

        // Get command argument player
        final Player target = getServer().getPlayer(args[0]);

        // Set command details
        String colour = "e";
        String pastTense = "poked";

        // Notify sender
        assert target != null;
        player.sendMessage("§" + colour + "You " + pastTense + " §l" + target.getName() + "§r§" + colour + "!");

        // Notify argument player
        target.sendMessage("§" + colour + "§l" + player.getName() + "§r §" + colour + pastTense + " you!");

        // Emit particles on both players
        player.getWorld().spawnParticle(Particle.CRIT, player.getLocation(), 3, 0.5, 0.5, 0.5, 0);
        target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 3, 0.5, 0.5, 0.5, 0);

        // Play sound to both players
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);

        // Output command output
        return true;
    }

}
