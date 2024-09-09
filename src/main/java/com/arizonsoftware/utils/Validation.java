package com.arizonsoftware.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Validation {

    /**
     * Checks if a command is enabled.
     *
     * @param command the command to check
     * @param sender  the command sender
     * @return true if the command is enabled, false otherwise
     */
    public static boolean checkIsEnabled(@NotNull Command command, @NotNull CommandSender sender) {
        Boolean isEnabled = Configuration.getBoolean("commands.yml", "commands." + command.getLabel() + ".enabled");
        if (!isEnabled) {
            sender.sendMessage(MessageHandler.parseColor(MessageHandler.get("message_error_command_disabled")));
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
    public static boolean checkIsSenderPlayer(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageHandler.parseColor(MessageHandler.get("message_error_player_only")));
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
        // Check if executing player specified themselves
        if (args[0].equals(sender.getName()) && Configuration.getBoolean("config.yml", "allow-self-executions") == false) {
            sender.sendMessage(MessageHandler.parseColor(MessageHandler.get("message_error_player_self")));
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
            sender.sendMessage(MessageHandler.parseColor(MessageHandler.get("message_error_command_syntax.player")));
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
    public static boolean checkIsTargetOnline(@NotNull CommandSender sender, @NotNull String[] args) {
        if (Bukkit.getServer().getPlayer(args[0]) == null) {
            sender.sendMessage(MessageHandler.parseColor(MessageHandler.get("message_error_player_offline")));
            return false;
        } else {
            return true;
        }
    }
}
