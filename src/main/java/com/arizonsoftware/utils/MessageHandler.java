package com.arizonsoftware.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import com.arizonsoftware.Main;

public class MessageHandler {

    /**
     * Formats a message by replacing placeholders with
     * corresponding replacements.
     *
     * @param message      the original message with
     *                     placeholders
     * @param subjects     an array of subjects to be replaced
     * @param replacements an array of replacements for the
     *                     subjects
     * @param convertColor a flag indicating whether the message
     *                     should be color formatted
     * @return the formatted message with placeholders replaced
     */
    public static String format(String message, String[] subjects, String[] replacements, boolean convertColor) {
        // Replace all occurrences of subjects with their
        // replacements
        for (int i = 0; i < subjects.length; i++) {
            if (replacements[i] == null) {
                Bukkit.getLogger().severe("[MessageHandler] Replacement for subject '" + subjects[i] + "' is null.");
                continue; // Skip this replacement
            }
            message = message.replace("%" + subjects[i] + "%", replacements[i]);
        }
        // Determine if string should be color formatted
        return convertColor ? MessageHandler.parseColor(message) : message;
    }

    /**
     * Retrieves the value of a language key.
     *
     * @param key The key of the language value to retrieve.
     * @return The value of the language key.
     */
    public static String get(String key) {
        // Create new config instance
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "config.yml"));

        // Determine language from config.yml
        String language = config.getString("language");

        // Get language file
        String currentLangKey = Configuration.getString("lang/" + language + ".yml", key);

        // Get fallback language file
        String fallbackLangKey = Configuration.getString("lang/en.yml", key);

        // Check if requestedKey is null
        if (currentLangKey == null) {
            Bukkit.getServer().getLogger().warning("[" + Main.getInstance().getName() + "] Could not locate key in " + Configuration.getString("config.yml", "language") + ".yml: " + key + ". Attempting to locate fallback data..");
            Debugging.logToFile(Configuration.class.getSimpleName(), "Could not locate key in " + Configuration.getString("config.yml", "language") + ".yml: " + key + ". Attempting to locate fallback data..");
            if (fallbackLangKey != null) {
                Bukkit.getServer().getLogger().warning("[" + Main.getInstance().getName() + "] Found fallback language data in en.yml: " + key);
                Debugging.logToFile(Configuration.class.getSimpleName(), "Found fallback language data in en.yml: " + key);
                return fallbackLangKey;
            } else {
                Bukkit.getServer().getLogger().warning("[" + Main.getInstance().getName() + "] Failed to locate fallback language data in en.yml: " + key);
                Debugging.logToFile(Configuration.class.getSimpleName(), "Failed to locate fallback language data in en.yml: " + key);
                return "MISSING_LANGUAGE_KEY";
            }
        }

        // Return language file value
        return currentLangKey;
    }

    /**
     * Replaces all occurrences of the ampersand character '&'
     * with the section symbol '§' in the given string.
     *
     * @param string the string to be parsed
     * @return the parsed string with ampersands replaced by
     *         section symbols
     */
    @SuppressWarnings("deprecation")
    public static String parseColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}