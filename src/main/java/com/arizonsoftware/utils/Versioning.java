package com.arizonsoftware.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.bukkit.Bukkit;
import org.yaml.snakeyaml.Yaml;

import com.arizonsoftware.Main;

public class Versioning {

    public static String configured = Configuration.getString("config.yml", "config-version");
    public static String current = Main.getInstance().getPluginMeta().getVersion();
    public static String latest = getLatest();

    /**
     * Checks the plugin version and logs a warning if outdated
     * or an info message if up-to-date, based on the
     * "check-for-latest" configuration. Uses provided subjects
     * and replacements for message formatting.
     */
    public static void checkVersion() {
        if (Main.getInstance().getConfig().getBoolean("check-for-latest")) {
            String[] subjects = { "current_version", "latest_version", "source_url" };
            String[] replacements = { current, latest, Main.getInstance().getPluginMeta().getWebsite() };

            if (!isLatest()) {
                Bukkit.getLogger().warning(MessageHandler.format(MessageHandler.get("message_context_command_version.outdated-notify"), subjects, replacements, true));
            } else {
                Bukkit.getLogger().info(MessageHandler.format(MessageHandler.get("message_context_command_version.up-to-date-notify"), subjects, replacements, true));
            }
        }
    }

    /**
     * Checks if the current version is the latest version.
     * 
     * @return true if the current version is the latest
     *         version, false otherwise.
     */
    public static boolean isLatest() {
        if (current == null || latest == null) {
            return false;
        }
        if (current.equals(latest)) {
            return true;
        }
        return compareVersions(current, latest) > 0;
    }

    /**
     * Compares two version strings.
     *
     * @param version1 the first version string to compare
     * @param version2 the second version string to compare
     * @return a negative integer if version1 is lower than
     *         version2, a positive integer if version1 is
     *         greater than version2, or zero if the versions
     *         are equal
     */
    private static int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");
        int length = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < length; i++) {
            int part1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int part2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (part1 != part2) {
                return part1 - part2;
            }
        }
        return 0;
    }

    /**
     * Retrieves the latest version of the software.
     * 
     * @return The latest version of the software.
     */
    public static String getLatest() {
        String version = current;
        try {
            version = fetchLatest();
            if (version == null) {
                version = current;
            }
        } catch (Exception e) {
            Debugging.log(Versioning.class.getSimpleName(), "Error fetching latest version: " + e.getMessage());
            e.printStackTrace();
        }
        return version;
    }

    /**
     * Fetches the latest version from a remote URL.
     * 
     * @return The latest version as a String.
     * @throws Exception If there is an error reading or parsing
     *                   the latest version.
     */
    @SuppressWarnings("deprecation")
    private static String fetchLatest() throws Exception {
        String url = "https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/src/main/resources/config.yml";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }

            Yaml yaml = new Yaml();
            Object data = yaml.load(response.toString());
            if (data instanceof Map) {
                Map<String, Object> yamlMap = yaml.loadAs(response.toString(), Map.class);
                return (String) yamlMap.get("config-version");
            }
        } catch (Exception e) {
            Debugging.log(Versioning.class.getSimpleName(), "Error reading or parsing latest version: " + e.getMessage());
            throw e;
        }
        return null;
    }
}