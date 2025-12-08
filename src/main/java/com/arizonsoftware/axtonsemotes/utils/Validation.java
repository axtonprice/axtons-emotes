package com.arizonsoftware.axtonsemotes.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Provides utility methods for validating commands, arguments, players, and emote configuration.
 */
public final class Validation {

    private static final String ERROR_COMMAND_DISABLED = "error.command.disabled";
    private static final String ERROR_PLAYER_ONLY = "error.player.only";
    private static final String ERROR_PLAYER_SELF = "error.player.self";
    private static final String ERROR_SYNTAX_PLAYER = "error.command.syntax.no_target";
    private static final String ERROR_PLAYER_OFFLINE = "error.player.offline";

    private Validation() {
        throw new IllegalStateException("Utility class");
    }

    /**
    * Validates that an emote configuration is complete and correct.
    *
    * @param emoteName The name of the emote to validate.
    * @return true if the emote configuration exists and contains all required fields; false otherwise.
    */
    public static boolean validateEmoteConfig(String emoteName) {
        // Load emotes.yml
        YamlConfiguration config = Configuration.getConfig("emotes.yml");
        if (config == null)
            return false;

        // Determine which section this emote belongs to
        String basePath;
        if (config.isSet("expressions." + emoteName)) {
            basePath = "expressions." + emoteName;
        } else if (config.isSet("shared-emotes." + emoteName)) {
            basePath = "shared-emotes." + emoteName;
        } else {
            return false; // Emote not found
        }

        ConfigurationSection section = config.getConfigurationSection(basePath);
        if (section == null)
            return false;

        // Required fields
        if (!section.contains("enabled"))
            return false;

        String type = config.getString(basePath + ".type");
        if (type == null) {
            // If type is missing in expressions, we can infer it
            type = basePath.startsWith("expressions.") ? "expression" : "shared";
        } else if (!type.equalsIgnoreCase("shared") && !type.equalsIgnoreCase("expression")) {
            return false;
        }

        // Check required fields
        if (!section.contains("messages.sender"))
            return false;
        if (!section.contains("messages.global") && !section.contains("messages.target"))
            return false;
        if (!section.contains("effects.particle"))
            return false;
        if (!section.contains("effects.sound"))
            return false;

        // All validations passed
        return true;
    }

    /**
    * Checks if a command is enabled in the configuration.
    *
    * @param command The command to check.
    * @param sender The sender attempting to execute the command.
    * @return true if the command is enabled; false otherwise.
    */
    public static boolean checkIsEnabled(@NotNull Command command, @NotNull CommandSender sender) {
        String commandLabel = command.getLabel();
        YamlConfiguration config = Configuration.getConfig("emotes.yml");
        if (config == null) {
            Debugging.log(Validation.class.getSimpleName(),
                    "Failed to load emotes.yml when checking command: " + commandLabel);
            return false;
        }

        // Determine which section the emote belongs to
        String basePath;
        if (config.isSet("expressions." + commandLabel)) {
            basePath = "expressions." + commandLabel;
        } else if (config.isSet("shared-emotes." + commandLabel)) {
            basePath = "shared-emotes." + commandLabel;
        } else {
            Debugging.log(Validation.class.getSimpleName(),
                    "Command attempt failed: Unknown emote '" + commandLabel + "' by " + sender.getName());
            sender.sendMessage(MessageHandler.parseError("That emote does not exist."));
            return false;
        }

        boolean isEnabled = config.getBoolean(basePath + ".enabled", false);
        if (!isEnabled) {
            Debugging.log(Validation.class.getSimpleName(),
                    "Command attempt failed: Disabled emote '" + commandLabel + "' by " + sender.getName());
            sender.sendMessage(MessageHandler.parseError(ERROR_COMMAND_DISABLED));
            return false;
        }

        return true;
    }

    /**
    * Checks if the sender of a command is a player.
    *
    * @param sender The command sender to check.
    * @return true if the sender is a player; false otherwise.
    */
    public static boolean checkIsSenderPlayer(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            Debugging.log(
                    Validation.class.getSimpleName(),
                    "Command attempt failed: Non-player '" + sender.getName()
                            + "' tried to execute player-only command");
            sender.sendMessage(MessageHandler.parseError(ERROR_PLAYER_ONLY));
            return false;
        }
        return true;
    }

    /**
    * Checks whether a command is being executed on oneself when self-execution is disallowed.
    *
    * @param sender The sender executing the command.
    * @param args The command arguments.
    * @return true if self-execution is allowed; false if disallowed.
    */
    public static boolean checkSelfExecution(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args[0].equals(sender.getName()) && !Configuration.getBoolean("config.yml", "allow-self-executions")) {
            Debugging.log(
                    Validation.class.getSimpleName(),
                    "Command attempt failed: Self-execution not allowed for " + sender.getName());
            sender.sendMessage(MessageHandler.parseError(ERROR_PLAYER_SELF));
            return false;
        }
        return true;
    }

    /**
    * Checks that at least one argument was provided for a command.
    *
    * @param sender The command sender.
    * @param args The command arguments.
    * @return true if arguments are present; false otherwise.
    */
    public static boolean checkArguments(@NotNull CommandSender sender, String[] args) {
        if (args.length == 0) {
            Debugging.log(
                    Validation.class.getSimpleName(),
                    "Command attempt failed: No arguments provided by " + sender.getName());
            sender.sendMessage(MessageHandler.parseError(ERROR_SYNTAX_PLAYER));
            return false;
        }
        return true;
    }

    /**
    * Checks whether a target player specified in the command arguments is online.
    *
    * @param sender The command sender.
    * @param args The command arguments (first argument is assumed to be the target player's name).
    * @return true if the target player is online; false otherwise.
    */
    public static boolean checkIsTargetOnline(@NotNull CommandSender sender, @NotNull String[] args) {
        if (Bukkit.getPlayer(args[0]) == null) {
            Debugging.log(Validation.class.getSimpleName(),
                    "Command attempt failed: Target player '" + args[0] + "' is offline, requested by "
                            + sender.getName());
            sender.sendMessage(MessageHandler.parseError(ERROR_PLAYER_OFFLINE));
            return false;
        }
        return true;
    }
}
