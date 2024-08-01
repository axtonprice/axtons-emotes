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

public class Stare implements CommandExecutor {

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

        // Notify sender
        player.sendMessage(Strings.ParseColors("&7You stare at &l" + target.getName() + "&r&7!"));

        // Notify argument player
        target.sendMessage(Strings
                .ParseColors("&7&l" + player.getName() + "&r &7stares at you!"));

        // Play sound to both players
        player.getWorld().playSound(player.getLocation(), Sound.AMBIENT_CAVE, 0.5F, 1);
        target.getWorld().playSound(target.getLocation(), Sound.AMBIENT_CAVE, 0.5F, 1);

        // Output command output
        return true;
    }

}
