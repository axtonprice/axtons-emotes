package com.arizonsoftware.axtonsemotes.commands;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import com.arizonsoftware.axtonsemotes.utils.Configuration;
import com.arizonsoftware.axtonsemotes.utils.Debugging;
import com.arizonsoftware.axtonsemotes.utils.MessageHandler;
import com.arizonsoftware.axtonsemotes.utils.Registry;
import com.arizonsoftware.axtonsemotes.utils.Versioning;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;

public class UtilityCommands {

   private static final String ERROR_NO_PERMISSION = "error.permission";
   private static final String ERROR_COMMAND_EXECUTE = "error.command.execute";
   private static final String COMMAND_RELOAD = "command.reload.success";
   private static final String COMMAND_RELOAD_NOTE = "command.reload.emotes_note";
   private static final String COMMAND_VERSION_LATEST = "command.version.info_latest";
   private static final String COMMAND_VERSION_OUTDATED = "command.version.info_outdated";
   private static final String COMMAND_VERSION_RELEASES = "command.version.info_releases";

   private final YamlConfiguration configYML = Configuration.getConfig("config.yml");
   private final AxtonsEmotes instance = AxtonsEmotes.getInstance();

   /**
    * Reloads configuration, emote files, language files, and debug settings.
    * Sends feedback to the sender and logs the reload.
    *
    * @param sender The command sender executing the reload.
    */
   public void reloadConfig(CommandSender sender) {
      // Check permissions
      if (!validatePermissions("reload", sender))
         return;

      // Timings
      long startTime = System.currentTimeMillis();

      // Save default configs if not exist
      Configuration.saveDefaultConfigs();

      // Reload main config
      instance.reloadConfig();
      String[] configFiles = { "config.yml", "emotes.yml" };

      // Reload each config file
      for (String configFile : configFiles) {
         Configuration.reloadConfig(configFile);
         Debugging.log(classMethod(), sender.getName() + " reloaded config: '" + configFile + "'");
      }

      // Reload active language file
      Configuration.reloadConfig(MessageHandler.getLanguageFileName());

      MessageHandler.reload();
      Debugging.log(classMethod(), sender.getName() + " reloaded language files.");

      Debugging.reloadConfig();
      Debugging.log(classMethod(), sender.getName() + " reloaded debug config.");

      Registry.registerAll(startTime);

      sender.sendMessage(MessageHandler.parseDefaultReplace(
            COMMAND_RELOAD,
            new String[] { "exec_time" },
            new String[] { String.valueOf(System.currentTimeMillis() - startTime) }));
      sender.sendMessage(MessageHandler.parseInfo(COMMAND_RELOAD_NOTE));
   }

   /**
    * Sends the current plugin version and the latest available version to the sender.
    * Includes clickable link for players.
    *
    * @param command The command being executed.
    * @param sender  The command sender requesting version info.
    */
   public void pluginVersion(String command, CommandSender sender) {

      // Check permissions
      if (!validatePermissions(command, sender))
         return;

      // Fetch versions
      String current = Versioning.installedVersion;
      String latest = Versioning.latestSourceVersion;
      String releasesURL = instance.getDescription().getWebsite() + "/releases";

      // Determine message key based on version status
      boolean outdated = Versioning.isOutdated();
      String msgKey = outdated ? COMMAND_VERSION_OUTDATED : COMMAND_VERSION_LATEST;

      // Format message
      String formattedMessage = MessageHandler.parseDefaultReplace(
            msgKey,
            new String[] { "current_version", "latest_version" },
            new String[] { current, latest });

      // Send message with clickable link if sender is a player
      if (sender instanceof Player player) {
         try {

            // Build the base message
            TextComponent message = new TextComponent(formattedMessage + " ");

            // Build clickable link
            TextComponent link = new TextComponent(
                  MessageHandler.parseColor("&b&n" + MessageHandler.get(COMMAND_VERSION_RELEASES)));
            link.setColor(net.md_5.bungee.api.ChatColor.AQUA); // Optional, already colored with parseColor
            link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, releasesURL));

            // Append link to the message
            message.addExtra(link);

            // Send to player
            player.spigot().sendMessage(message);
         } catch (Exception e) {
            // Fallback: just send plain text if URI fails
            player.sendMessage(formattedMessage + " Update at " + releasesURL);
         }
      } else {
         sender.sendMessage(formattedMessage + " Update at " + releasesURL);
      }

      Debugging.log(getClass().getSimpleName(),
            "Version check: current: " + current + ", latest: " + latest);
   }

   /**
    * Toggles a boolean value in the configuration and saves it.
    * Sends feedback to the sender and logs the change.
    *
    * @param configKey The configuration key to toggle.
    * @param command   The associated command for permission check.
    * @param sender    The sender executing the toggle.
    */
   public void toggleConfig(String configKey, String command, CommandSender sender) {
      if (!validatePermissions(command, sender))
         return;

      boolean currentStatus = configYML.getBoolean(configKey);
      configYML.set(configKey, !currentStatus);

      sender.sendMessage(MessageHandler.parseSuccess(
            currentStatus ? "command." + command + ".disabled"
                  : "command." + command + ".enabled"));

      saveConfig(sender);
      Debugging.log(classMethod(), sender.getName() +
            " updated config '" + configKey + "' to: " + configYML.getBoolean(configKey));
   }

   /**
    * Saves the main config file to disk.
    *
    * @param sender The sender to notify on error.
    */
   private void saveConfig(CommandSender sender) {
      try {
         configYML.save(instance.getDataFolder() + "/config.yml");
      } catch (IOException e) {
         sender.sendMessage(MessageHandler.parseError(ERROR_COMMAND_EXECUTE));
         e.printStackTrace();
      }
   }

   /**
    * Resets all language files to their default values.
    * Creates language directories if necessary.
    *
    * @param sender The sender executing the reset.
    */
   public void resetLangFiles(CommandSender sender) {

      // Check permissions
      if (!validatePermissions("resetlang", sender))
         return;

      // Fetch language files
      String langFolder = "lang";
      String[] langFiles = Configuration.languages;

      try {
         // Create directories if they don't exist
         Configuration.createDirectory(instance.getDataFolder().toPath());
         Configuration.createDirectory(instance.getDataFolder().toPath().resolve(langFolder));

         // Save default language files and replace
         Configuration.saveDefaultConfigFile("lang/en.yml", false);
         Arrays.stream(langFiles)
               .forEach(langFile -> Configuration.saveDefaultConfigFile("lang/translations/" + langFile, true));

         // Reload language files after reset
         MessageHandler.reloadLanguageFiles();

      } catch (Exception e) {
         Debugging.logError("Configuration setup failed", e);
      }

      // Notify sender and log
      sender.sendMessage(MessageHandler.parseSuccess("command.resetlang.success"));
      Debugging.log(classMethod(), sender.getName() + " reset language files to default.");
   }

   /**
    * Sends a help message with a clickable link to the plugin wiki.
    *
    * @param sender The sender requesting help.
    */
   public void helpCommand(CommandSender sender) {
      // Check permissions
      if (!validatePermissions("help", sender))
         return;

      String url = AxtonsEmotes.getInstance().getDescription().getWebsite() + "/wiki";

      // Build base message
      TextComponent message = new TextComponent(MessageHandler.parseSuccess("command.help"));

      // Apply italics formatting
      message.setItalic(true);

      // Set click event to open URL
      message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

      if (sender instanceof Player player) {
         // Send clickable message to player
         player.spigot().sendMessage(message);
      } else {
         // Fallback for console
         sender.sendMessage(MessageHandler.parseSuccess("command.help") + ": " + url);
      }
   }

   /**
    * Checks if the sender has permission to execute the command.
    *
    * @param command The command to check permissions for.
    * @param sender  The sender executing the command.
    * @return True if the sender has permission, false otherwise.
    */
   private boolean validatePermissions(String command, CommandSender sender) {
      String permission = instance.getName().toLowerCase() + ".admin." + command;

      if (sender.hasPermission(permission))
         return true;

      Debugging.log(classMethod(),
            sender.getName() + " lacks permission '" + permission + "' for command '" + command + "'");
      sender.sendMessage(MessageHandler.parseError(ERROR_NO_PERMISSION));
      return false;
   }

   /**
    * Returns a consistent class/method tag for debugging logs.
    *
    * @return String in format ClassName/methodName.
    */
   private String classMethod() {
      return getClass().getSimpleName() + "/" + Thread.currentThread().getStackTrace()[2].getMethodName();
   }
}
