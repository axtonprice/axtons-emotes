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
      YamlConfiguration config = Configuration.getConfig("emotes.yml");
      String basePath = "commands." + emoteName;
      ConfigurationSection section = config.getConfigurationSection(basePath);
      if (section == null)
         return false;

      if (!section.contains("enabled"))
         return false;
      if (!section.contains("type"))
         return false;

      String emoteType = Configuration.getString("emotes.yml", basePath + ".type");
      if (emoteType == null)
         return false;
      if (!emoteType.equalsIgnoreCase("shared") && !emoteType.equalsIgnoreCase("expression"))
         return false;

      if (!section.contains("messages.player"))
         return false;
      if (!section.contains("messages.target"))
         return false;
      if (!section.contains("effects.particle"))
         return false;

      return section.contains("effects.sound");
   }

   /**
    * Checks if a command is enabled in the configuration.
    *
    * @param command The command to check.
    * @param sender The sender attempting to execute the command.
    * @return true if the command is enabled; false otherwise.
    */
   public static boolean checkIsEnabled(@NotNull Command command, @NotNull CommandSender sender) {
      boolean isEnabled = Configuration.getBoolean("emotes.yml", "commands." + command.getLabel() + ".enabled");
      if (!isEnabled) {
         Debugging.log(
               logContext("checkIsEnabled"),
               "Command attempt failed: Disabled command '" + command.getLabel() + "' by " + sender.getName());
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
               logContext("checkIsSenderPlayer"),
               "Command attempt failed: Non-player '" + sender.getName() + "' tried to execute player-only command");
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
               logContext("checkSelfExecution"),
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
               logContext("checkArguments"),
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
         Debugging.log(
               logContext("checkIsTargetOnline"),
               "Command attempt failed: Target player '" + args[0] + "' is offline, requested by " + sender.getName());
         sender.sendMessage(MessageHandler.parseError(ERROR_PLAYER_OFFLINE));
         return false;
      }
      return true;
   }

   /**
    * Constructs a logging context string for internal debug messages.
    *
    * @param method The name of the method invoking the log.
    * @return A string in the format "Validation/methodName".
    */
   private static String logContext(String method) {
      return Validation.class.getSimpleName() + "/" + method;
   }
}
