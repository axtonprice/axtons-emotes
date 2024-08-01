package com.arizonsoftware.commands.shared;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.utils.Strings;
import com.arizonsoftware.utils.Validation;

import static org.bukkit.Bukkit.getServer;

public class DapUp implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        // Execution validation
        if (!Validation.checkIsPlayer(sender))
            return true;
        if (!Validation.checkHasPermission(sender, getClass()))
            return true;
        if (!Validation.checkArguments(sender, args))
            return true;
        if (!Validation.checkSelfExecution(sender, args))
            return true;
        if (!Validation.checkIsOnline(sender, args))
            return true;

        // Get player and target
        final Player target = getServer().getPlayer(args[0]);
        final Player player = (Player) sender;
        assert target != null;

        // Get random whole number from 0 to 9
        int random = (int) (Math.random() * 4);

        // Determine if the dap up was a good clap
        String result;
        Sound sound;
        if (random == 1) {
            result = "&a&lGood clap!";
            sound = Sound.ITEM_MACE_SMASH_GROUND_HEAVY;
        } else if (random == 2) {
            result = "&e&lNear miss!";
            sound = Sound.ITEM_MACE_SMASH_AIR;
        } else {
            result = "&c&lMiss!";
            sound = Sound.ENTITY_EVOKER_AMBIENT;
        }

        // Notify sender
        player.sendMessage(Strings.ParseColors("&bYou try to dap up &l" + target.getName() + "&r&b! " + result));

        // Notify argument player
        target.sendMessage(Strings.ParseColors("&b&l" + player.getName() + "&r &bdapped you up! " + result));

        // Emit particles on both players
        player.getWorld().spawnParticle(Particle.CRIT, player.getLocation(), 3, 0.5, 0.5, 0.5, 0);
        target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 3, 0.5, 0.5, 0.5, 0);

        // Play sound to both players
        player.getWorld().playSound(player.getLocation(), sound, 1, 1);
        target.getWorld().playSound(target.getLocation(), sound, 1, 1);

        // Output command output
        return true;
    }

}
