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

public class Stab implements CommandExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                        @NotNull String[] args) {

                // Execution validation
                if (!Validation.checkHasPermission(sender, getClass()))
                        return true;
                if (!Validation.checkArguments(sender, args))
                        return true;
                if (!Validation.checkIsPlayer(sender))
                        return true;
                if (!Validation.checkSelfExecution(sender, args))
                        return true;
                if (!Validation.checkIsOnline(sender, args))
                        return true;

                // Get player and target
                final Player target = getServer().getPlayer(args[0]);
                final Player player = (Player) sender;
                assert target != null;

                // Notify sender
                player.sendMessage(Strings
                                .ParseColors("&cYou stabbed &l" + target.getName() + "&r&c!"));

                // Notify argument player
                target.sendMessage(Strings
                                .ParseColors("&c&l" + player.getName() + "&r &cstabbed you!"));

                // Emit particles on both players
                player.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, player.getLocation(), 3, 0.5, 0.5, 0.5, 0);
                target.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, target.getLocation(), 3, 0.5, 0.5, 0.5, 0);

                // Play sound to both players
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GUARDIAN_FLOP, 1, 1);
                try {
                        Thread.sleep(500);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GUARDIAN_FLOP, 1, 1);

                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GUARDIAN_FLOP, 1, 1);
                try {
                        Thread.sleep(500);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GUARDIAN_FLOP, 1, 1);

                // Output command output
                return true;
        }

}
