package com.arizonsoftware.axtonsemotes.utils;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.util.Map;

/**
 * Handles plugin version tracking, comparison, migration, and update notifications.
 */
public class Versioning {

   // Version fields
   public static String installedVersion = AxtonsEmotes.getInstance().getDescription().getVersion();
   public static String latestSourceVersion = getLatestSourceVersion();
   private static final String CONFIG_FILE_NAME = Configuration.CONFIG_FILE_NAME;

   private Versioning() {
      throw new IllegalStateException("Utility class");
   }

   /**
    * Checks the plugin version against the latest released version and logs warnings if outdated.
    * Also triggers migration if necessary.
    */
   public static void checkForUpdates() {

      // Check if config.yml exists
      File configFile = new File(AxtonsEmotes.getInstance().getDataFolder(), CONFIG_FILE_NAME);
      if (!configFile.exists()) {
         Configuration.saveDefaultConfigFile(CONFIG_FILE_NAME, false);
      }

      // Skip update check if disabled in config
      if (!Configuration.getBoolean(CONFIG_FILE_NAME, "updates.check-for-latest", true)) {
         return;
      }

      // Debug logging
      Debugging.log(Versioning.class.getSimpleName(), MessageHandler.get("plugin.startup.version_check"));

      // Log warning if outdated
      if (isOlder(installedVersion, latestSourceVersion)) {
         String[] subjects = { "current_version", "latest_version" };
         String[] replacements = { installedVersion, latestSourceVersion };
         Debugging.raw("warning",
               MessageHandler.format(MessageHandler.get("command.version.outdated_notify"), subjects, replacements));
      }

      // Disable plugin if installedVersion cannot be verified
      if (installedVersion == null) {
         Debugging.raw("severe", "Unable to verify plugin version, plugin will shutdown.");
         Bukkit.getPluginManager().disablePlugin(AxtonsEmotes.getInstance());
         return;
      }

      // Fetch configured version
      String configuredVersion = Configuration.getString(CONFIG_FILE_NAME, "config-version", "0.0.0");

      // Reset config if version too old
      if (configuredVersion.equals("1.3.0")) {
         Debugging.raw("warning", "Config version is too old (1.3.0). Resetting to plugin defaults...");

         if (configFile.exists()) {
            try {
               Files.delete(configFile.toPath());
               Debugging.log(
                     Versioning.class.getSimpleName() + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
                     "Deleted configuration file: " + configFile);
            } catch (IOException e) {
               Debugging.logError("Failed to delete configuration file: " + configFile, e);
            }
         }
         Configuration.saveDefaultConfigFile(CONFIG_FILE_NAME, true);
         Configuration.reloadConfig(CONFIG_FILE_NAME);

         return;
      }

      // Perform migration if installed version differs from config version
      if (!installedVersion.equals(configuredVersion)) {
         Debugging.raw("warning", "Config version is outdated (" + configuredVersion + " → "
               + installedVersion + "). Migrating configuration...");
         Migration.migrateToLatest();
      }

   }

   /**
    * Determines whether the installed version is the latest compared to the source.
    *
    * @return true if installed version is equal or newer than the latest source version.
    */
   public static boolean isLatest() {
      if (installedVersion == null || latestSourceVersion == null)
         return false;
      return compareVersions(installedVersion, latestSourceVersion) >= 0;
   }

   /**
    * Determines whether the installed version is outdated compared to the latest source version.
    *
    * @return true if installed version is older than the latest source version.
    */
   public static boolean isOutdated() {
      if (installedVersion == null || latestSourceVersion == null)
         return false;
      return compareVersions(installedVersion, latestSourceVersion) < 0;
   }

   /**
    * Checks if version v1 is older than v2.
    *
    * @param v1 The first version.
    * @param v2 The second version.
    * @return true if v1 < v2.
    */
   public static boolean isOlder(String v1, String v2) {
      return compareVersions(v1, v2) < 0;
   }

   /**
    * Checks if version v1 is newer than v2.
    *
    * @param v1 The first version.
    * @param v2 The second version.
    * @return true if v1 > v2.
    */
   public static boolean isNewer(String v1, String v2) {
      return compareVersions(v1, v2) > 0;
   }

   /**
    * Compares two semantic version strings.
    *
    * @param v1 The first version string.
    * @param v2 The second version string.
    * @return Negative if v1 < v2, zero if equal, positive if v1 > v2.
    */
   private static int compareVersions(String v1, String v2) {
      if (v1 == null || v2 == null) {
         Debugging.log("Versioning/compareVersions", "Null version detected: v1=" + v1 + ", v2=" + v2);
         return 0;
      }

      String[] a = v1.split("\\.");
      String[] b = v2.split("\\.");
      int len = Math.max(a.length, b.length);

      for (int i = 0; i < len; i++) {
         int x = i < a.length ? parseIntSafe(a[i]) : 0;
         int y = i < b.length ? parseIntSafe(b[i]) : 0;
         if (x != y)
            return x - y;
      }

      return 0;
   }

   /**
    * Parses an integer from a string safely, returning 0 if parsing fails.
    *
    * @param s The string to parse.
    * @return Parsed integer or 0 if invalid.
    */
   private static int parseIntSafe(String s) {
      try {
         return Integer.parseInt(s.replaceAll("[^0-9]", ""));
      } catch (Exception e) {
         return 0;
      }
   }

   /**
    * Sends an update notification to a player if the plugin is outdated and player has permission.
    *
    * @param player The player to notify.
    */
   public static void notifyPlayer(Player player) {

      // Check notification settings
      if (!Configuration.getBoolean(CONFIG_FILE_NAME, "updates.notify-players-on-update", true))
         return;

      // Check if player has permission to receive update notifications
      if (!(player.hasPermission("axtonsemotes.admin.updatenotify") || player.isOp()))
         return;

      // Check if the plugin is outdated
      if (!isOutdated())
         return;

      // Fetch version text
      String[] subjects = { "current_version", "latest_version" };
      String[] replacements = { installedVersion, latestSourceVersion };

      // Fetch message text
      String repoURL = AxtonsEmotes.getInstance().getDescription().getWebsite();
      String messageText = MessageHandler.parseInfoReplace("command.version.outdated_notify", subjects, replacements);

      // Build base message
      TextComponent message = new TextComponent(messageText + " ");

      // Build clickable link
      TextComponent link = new TextComponent(ChatColor.AQUA + "" + ChatColor.UNDERLINE +
            MessageHandler.get("command.version.info_releases"));
      link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, repoURL));

      // Combine message and link
      message.addExtra(link);

      // Send clickable message to player
      player.spigot().sendMessage(message);

   }

   /**
    * Retrieves the latest version string from the source repository.
    *
    * @return The latest version string or the installed version if retrieval fails.
    */
   public static String getLatestSourceVersion() {
      try {
         String found = fetchLatest();
         return found != null ? found : installedVersion;
      } catch (Exception e) {
         Debugging.log("Versioning/getLatest", e.getMessage());
         return installedVersion;
      }
   }

   /**
    * Fetches the latest config.yml from GitHub and extracts the "config-version".
    *
    * @return Latest version string from remote or null if unavailable.
    * @throws Exception If HTTP request or parsing fails.
    */
   private static String fetchLatest() throws Exception {
      String url = "https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/src/main/resources/config.yml";
      HttpURLConnection http = (HttpURLConnection) URI.create(url).toURL().openConnection();
      http.setRequestMethod("GET");

      StringBuilder output = new StringBuilder();
      try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
         String line;
         while ((line = br.readLine()) != null) {
            output.append(line).append("\n");
         }
      }

      Yaml yaml = new Yaml();
      Object result = yaml.load(output.toString());

      if (!(result instanceof Map<?, ?> map))
         return null;

      Object versionObj = map.get("config-version");
      return versionObj instanceof String ? (String) versionObj : null;
   }
}
