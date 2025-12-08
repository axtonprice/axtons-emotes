package com.arizonsoftware.axtonsemotes.utils;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public final class Migration {

    private Migration() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks plugin version and performs necessary migration steps.
     */
    public static void checkForMigrations() {

        File dataFolder = AxtonsEmotes.getInstance().getDataFolder();
        String configuredVersion = Configuration.getString("config.yml", "config-version", "0.0.0");
        String installed = Versioning.installedVersion;

        // Fully reset config.yml and emotes.yml for versions older than 1.4.0
        if (Versioning.isOlder(configuredVersion, "1.4.0")) {
            Debugging.raw("warning", MessageHandler.format(
                    MessageHandler.get("plugin.startup.migration.reset"),
                    new String[] { "version" },
                    new String[] { configuredVersion }));

            deleteLegacyFile(new File(dataFolder, "commands.yml"));
            Configuration.saveDefaultConfigFile("config.yml", true);
            Configuration.saveDefaultConfigFile("emotes.yml", false);
            return;
        }

        // Partially migrate config.yml and delete legacy commands.yml for version 1.4.0
        if (configuredVersion.equals("1.4.0")) {
            Debugging.raw("warning", MessageHandler.get("plugin.startup.migration.partial"));

            deleteLegacyFile(new File(dataFolder, "commands.yml"));
            migrateConfig();
            Configuration.saveDefaultConfigFile("emotes.yml", false);
            return;
        }

        // Fully migrate config.yml and emotes.yml for versions 1.5.0 and above
        if (!installed.equals(configuredVersion)) {
            Debugging.raw("warning", MessageHandler.format(
                    MessageHandler.get("plugin.startup.migration.migrate"),
                    new String[] { "old", "new" },
                    new String[] { configuredVersion, installed }));

            migrateConfig();

            // Pre-1.6.0 emotes.yml restructuring
            if (Versioning.isOlder(configuredVersion, "1.6.0")) {
                migrateEmotesPre160();
            }
        }
    }

    /**
     * Migrates the plugin's configuration file ("config.yml") to the latest format.
     *
     * @return {@code true} if migration was successful or not needed; {@code false} if migration failed.
     */
    public static boolean migrateConfig() {

        // Get data folder and config file
        File dataFolder = AxtonsEmotes.getInstance().getDataFolder();
        File configFile = new File(dataFolder, "config.yml");

        // Check if config.yml exists
        if (!configFile.exists()) {
            Debugging.log("Migration", "config.yml not found; skipping.");
            return true;
        }

        // Load old config.yml
        YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(configFile);
        YamlConfiguration defaultConfig;

        // Load default config.yml from jar
        try (InputStream is = AxtonsEmotes.getInstance().getResource("config.yml")) {
            if (is == null) {
                Debugging.log("Migration", "Default config.yml not found in plugin jar.");
                return false;
            }
            defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
        } catch (IOException e) {
            Debugging.log("Migration", "Failed to load default config.yml: " + e.getMessage());
            return false;
        }

        // Merge old config into default config
        mergeConfigs(oldConfig, defaultConfig, "");
        defaultConfig.set("config-version", Versioning.installedVersion);

        // Save migrated config
        try {
            defaultConfig.save(configFile);
        } catch (IOException e) {
            Debugging.log("Migration", "Failed to save migrated config: " + e.getMessage());
            return false;
        }

        Debugging.log("Migration", "Configuration file 'config.yml' successfully migrated!");
        return true;
    }

    /**
     * Migrates emotes.yml from pre-1.6.0 structure to new v1.6.0 structure.
     * 
     * @return {@code true} if migration was successful or not needed; {@code false} if migration failed.
     */
    public static boolean migrateEmotesPre160() {
        File dataFolder = AxtonsEmotes.getInstance().getDataFolder();
        File oldFile = new File(dataFolder, "emotes.yml");

        if (!oldFile.exists()) {
            Debugging.log("Migration", "No emotes.yml exists. Skipping migration.");
            return true;
        }

        // Load old emotes.yml
        YamlConfiguration oldCfg = YamlConfiguration.loadConfiguration(oldFile);
        YamlConfiguration newCfg;

        // Load default emotes.yml from jar
        try (InputStream is = AxtonsEmotes.getInstance().getResource("emotes.yml")) {
            if (is == null) {
                Debugging.log("Migration", "Default emotes.yml not found in jar.");
                return false;
            }
            newCfg = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
        } catch (IOException e) {
            Debugging.log("Migration", "Failed to load default emotes.yml: " + e.getMessage());
            return false;
        }

        // Clear old sections
        newCfg.set("expressions", null);
        newCfg.set("shared-emotes", null);

        // Create new sections
        ConfigurationSection expressions = newCfg.createSection("expressions");
        ConfigurationSection shared = newCfg.createSection("shared-emotes");

        // Check for commands section
        ConfigurationSection oldCmds = oldCfg.getConfigurationSection("commands");
        if (oldCmds == null) {
            Debugging.log("Migration", "Old emotes.yml missing 'commands' section. Aborted migration.");
            return false;
        }

        // Iterate and migrate each emote
        for (String emote : oldCmds.getKeys(false)) {
            ConfigurationSection oldEmote = oldCmds.getConfigurationSection(emote);
            if (oldEmote == null)
                continue;

            // Determine emote type and migrate accordingly
            String type = oldEmote.getString("type", "expression").toLowerCase();

            if (type.equals("expression")) {

                // Migrate to expressions section
                ConfigurationSection newEmote = expressions.createSection(emote);
                newEmote.set("enabled", oldEmote.get("enabled"));

                // Migrate messages
                ConfigurationSection msg = oldEmote.getConfigurationSection("messages");
                if (msg != null) {
                    ConfigurationSection m = newEmote.createSection("messages");
                    m.set("sender", msg.get("player"));
                    m.set("global", msg.get("target"));
                }

                // Migrate effects
                newEmote.set("effects.particle", oldEmote.get("effects.particle"));
                newEmote.set("effects.sound", oldEmote.get("effects.sound"));

            } else if (type.equals("shared")) {

                // Migrate to shared-emotes section
                ConfigurationSection newEmote = shared.createSection(emote);
                newEmote.set("enabled", oldEmote.get("enabled"));
                newEmote.set("messages.sender", oldEmote.get("messages.player"));
                newEmote.set("messages.target", oldEmote.get("messages.target"));
                newEmote.set("effects.particle", oldEmote.get("effects.particle"));
                newEmote.set("effects.sound", oldEmote.get("effects.sound"));
            }
        }

        try {
            // Save new emotes configuration
            newCfg.save(oldFile);

            // Insert a blank line before "shared-emotes:"
            List<String> lines = Files.readAllLines(oldFile.toPath(), StandardCharsets.UTF_8);
            try (BufferedWriter writer = Files.newBufferedWriter(oldFile.toPath(), StandardCharsets.UTF_8)) {
                for (String line : lines) {
                    if (line.startsWith("shared-emotes:")) {
                        writer.newLine(); // add blank line
                    }
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            Debugging.log("Migration", "Failed to save migrated emotes.yml: " + e.getMessage());
            return false;
        }

        // Migration complete
        Debugging.log("Migration", "Configuration file 'emotes.yml' migrated without adding missing defaults.");
        return true;
    }

    /**
     * Recursively merges old config values into the new configuration.
     */
    private static void mergeConfigs(YamlConfiguration oldCfg, YamlConfiguration newCfg, String path) {
        ConfigurationSection newSection = path.isEmpty() ? newCfg : newCfg.getConfigurationSection(path);
        if (newSection == null)
            return;

        // Recursively merge configuration sections
        for (String key : newSection.getKeys(false)) {
            String fullPath = path.isEmpty() ? key : path + "." + key;

            // If the key is a section, recurse into it
            if (newSection.isConfigurationSection(key)) {
                mergeConfigs(oldCfg, newCfg, fullPath);
            } else if (oldCfg.contains(fullPath)) {
                newCfg.set(fullPath, oldCfg.get(fullPath));
            }
        }
    }

    /**
     * Deletes the specified legacy file and logs the result.
     */
    private static void deleteLegacyFile(File file) {
        // Check if file exists
        if (!file.exists())
            return;

        // Delete the file
        try {
            Files.delete(file.toPath());
            Debugging.log(Versioning.class.getSimpleName(),
                    MessageHandler.format(
                            MessageHandler.get("plugin.startup.migration.deleting_file"),
                            new String[] { "file" },
                            new String[] { file.getName() }));
        } catch (IOException e) {
            // Log failure to delete
            Debugging.logError(
                    MessageHandler.format(
                            MessageHandler.get("plugin.startup.migration.deleting_file_failed"),
                            new String[] { "file" },
                            new String[] { file.getName() }),
                    e);
        }
    }
}
