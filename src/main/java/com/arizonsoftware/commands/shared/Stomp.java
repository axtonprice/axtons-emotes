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

public class Stomp implements CommandExecutor {

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
        int random = (int) (Math.random() * 6);

        // Determine if the dap up was a good clap
        String pastTense;
        if (random == 1) {
            pastTense = "shin-kicked";
        } else if (random == 2) {
            pastTense = "foot-stomped";
        } else if (random == 3) {
            pastTense = "knee-bashed";
        } else if (random == 4) {
            pastTense = "toe-crushed";
        } else if (random == 5) {
            pastTense = "rib-kicked";
        } else {
            pastTense = "kicked";
        }

        // Notify sender
        player.sendMessage(Strings
                .ParseColors("&cYou " + pastTense + " &l" + target.getName() + "&r&c!"));

        // Notify argument player
        target.sendMessage(
                Strings.ParseColors("&c&l" + player.getName() + "&r &c" + pastTense + " you!"));

        // Emit particles on both players
        player.getWorld().spawnParticle(Particle.CRIT, player.getLocation(), 3, 0.5, 1, 0.5, 0);
        target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 3, 0.5, 1, 0.5, 0);

        // Play sound to both players
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1, 1);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1, 1);

        // Output command output
        return true;
    }

}
