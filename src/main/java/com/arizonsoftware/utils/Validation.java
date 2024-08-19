package com.arizonsoftware.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.Main;

public class Validation {

    /**
     * Checks if a command is enabled.
     *
     * @param command the command to check
     * @param sender  the command sender
     * @return true if the command is enabled, false otherwise
     */
    public static boolean checkIsEnabled(@NotNull Command command, @NotNull CommandSender sender) {
        if (!Main.getInstance().getConfig()
                .getBoolean("commands." + command.getLabel() + ".enabled")) {
            sender.sendMessage(Strings.parse("&cThis command is disabled!"));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the given CommandSender is a player.
     *
     * @param sender The CommandSender to check.
     * @return true if the sender is a player, false otherwise.
     */
    public static boolean checkIsPlayer(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.parse("&4Error: &cOnly players can execute this command!"));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the given CommandSender has permission to execute the specified
     * command.
     *
     * @param sender  the CommandSender to check permission for
     * @param command the command to be executed
     * @return true if the sender has permission, false otherwise
     */
    public static boolean checkHasPermission(@NotNull CommandSender sender, Command command) {
        if (!sender.hasPermission("axtonsemotes." + command.getLabel())) {
            sender.sendMessage(Strings.parse("&4Error: &cYou don't have permission to execute this command!"));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the command execution is allowed for the specified sender and
     * arguments.
     *
     * @param sender the command sender
     * @param args   the command arguments
     * @return true if the command execution is allowed, false otherwise
     */
    public static boolean checkSelfExecution(@NotNull CommandSender sender, @NotNull String[] args) {
        // Check if self-executions are enabled in config
        FileConfiguration config = Main.getInstance().getConfig();

        // Check if executing player specified themselves
        if (args[0].equals(sender.getName()) && config.getBoolean("allowSelfExecutions") == false) {
            sender.sendMessage(Strings.parse("&4Error: &cYou can't execute this command on yourself!"));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks the arguments passed to a command.
     *
     * @param sender the command sender
     * @param args   the arguments passed to the command
     * @return true if the arguments are valid, false otherwise
     */
    public static boolean checkArguments(@NotNull CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Strings.parse("&4Error: &cYou must specify a valid player!"));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if a player is online.
     *
     * @param sender the command sender
     * @param args   the command arguments
     * @return true if the player is online, false otherwise
     */
    public static boolean checkIsOnline(@NotNull CommandSender sender, @NotNull String[] args) {
        if (Bukkit.getServer().getPlayer(args[0]) == null) {
            sender.sendMessage(Strings.parse("&4Error: &cPlayer not found or is offline!"));
            return false;
        } else {
            return true;
        }
    }

}
