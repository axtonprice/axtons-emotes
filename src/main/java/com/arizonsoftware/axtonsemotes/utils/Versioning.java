package com.arizonsoftware.axtonsemotes.utils;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Handles plugin version tracking, comparison, migration, and update notifications.
 */
public class Versioning {

    // Version fields
    public static String installedVersion = AxtonsEmotes.getInstance().getDescription().getVersion();
    public static String latestSourceVersion = getLatestSourceVersion();

    private Versioning() {
        throw new IllegalStateException("Utility class");
    }

    /**
    * Checks the plugin version against the latest released version and logs warnings if outdated.
    * Also triggers migration if necessary.
    */
    public static void checkForUpdates() {

        File dataFolder = AxtonsEmotes.getInstance().getDataFolder();
        File configFile = new File(dataFolder, "config.yml");

        // Ensure config.yml exists
        if (!configFile.exists()) {
            Configuration.saveDefaultConfigFile("config.yml", false);
        }

        // Disable plugin if installed version cannot be determined
        if (installedVersion == null) {
            Debugging.raw("severe", "Unable to verify plugin version, plugin will shutdown.");
            Bukkit.getPluginManager().disablePlugin(AxtonsEmotes.getInstance());
            return;
        }

        // Check for migrations
        Migration.checkForMigrations();

        // Skip update check if disabled in config
        if (!Configuration.getBoolean("config.yml", "updates.check-for-latest", true)) {
            return;
        }

        // Log version check start
        Debugging.log(Versioning.class.getSimpleName(), MessageHandler.get("plugin.startup.version_check"));

        // Warn if version is outdated, even after migration
        if (isOlder(installedVersion, latestSourceVersion)) {
            Debugging.raw("warning", MessageHandler.format(
                    MessageHandler.get("command.version.outdated_notify"),
                    new String[] { "current_version", "latest_version" },
                    new String[] { installedVersion, latestSourceVersion }));
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
        if (!Configuration.getBoolean("config.yml", "updates.notify-players-on-update", true))
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

        // Build base message
        String messageText = MessageHandler.parseInfoReplace("command.version.outdated_notify", subjects, replacements);
        TextComponent message = new TextComponent(messageText + " ");

        // Build clickable link
        String repoURL = AxtonsEmotes.getInstance().getDescription().getWebsite() + "/releases";
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
        http.setConnectTimeout(5_000); // 5s connect
        http.setReadTimeout(5_000); // 5s read

        int status = http.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            // not available / bad response
            return null;
        }

        StringBuilder output = new StringBuilder();
        try (InputStream in = http.getInputStream();
                InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line).append('\n');
            }
        }

        // Use LoaderOptions + SafeConstructor to avoid arbitrary object deserialization
        LoaderOptions options = new LoaderOptions();

        SafeConstructor constructor = new SafeConstructor(options);
        Yaml yaml = new Yaml(constructor);

        Object result = yaml.load(output.toString());
        if (!(result instanceof Map<?, ?> rawMap)) {
            return null;
        }

        Object versionObj = rawMap.get("config-version");
        return versionObj instanceof String ? (String) versionObj : null;
    }

}
