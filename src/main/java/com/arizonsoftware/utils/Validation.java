package com.arizonsoftware.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.Main;

public class Validation {

    public static boolean checkIsPlayer(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.ParseColors("&4Error: &cOnly players can execute this command!"));
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkHasPermission(@NotNull CommandSender sender, Class<?> command) {
        if (!sender.hasPermission("axtonsemotes." + command.getPackage() + "." + command.getSimpleName())) {
            sender.sendMessage(Strings.ParseColors("&4Error: &cYou don't have permission to execute this command!"));
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkSelfExecution(@NotNull CommandSender sender, @NotNull String[] args) {
        // Check if self-executions are enabled in config
        FileConfiguration config = Main.getInstance().getConfig();
        if (config.getBoolean("allowSelfExecutions"))
            return true;

        // Check if executing player specified themselves
        if (args[0].equals(sender.getName())) {
            sender.sendMessage(Strings.ParseColors("&4Error: &cYou can't execute this command on yourself!"));
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkArguments(@NotNull CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Strings.ParseColors("&4Error: &cYou must specify a valid player!"));
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkIsOnline(@NotNull CommandSender sender, @NotNull String[] args) {
        if (Bukkit.getServer().getPlayer(args[0]) == null) {
            sender.sendMessage(Strings.ParseColors("&4Error: &cPlayer not found or is offline!"));
            return false;
        } else {
            return true;
        }
    }

}
