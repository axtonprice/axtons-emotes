package com.arizonsoftware.axtonsemotes.utils;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Migration utility that updates the existing config.yml with new default keys
 * from the plugin's jar while preserving old values. Ensures config-version is
 * updated to the installed version.
 *
 * Note: Bukkit's YamlConfiguration does not preserve comments. For full comment
 * preservation, use SnakeYAML or a custom parser.
 */
public class Migration {

    private Migration() {
        throw new IllegalStateException("Utility class");
    }

    /**
    * Migrates the existing config.yml to the latest plugin default configuration.
    *
    * @return true if successful or skipped, false if an error occurred
    */
    public static boolean migrateToLatest() {
        File dataFolder = AxtonsEmotes.getInstance().getDataFolder();
        File oldFile = new File(dataFolder, "config.yml");

        // Skip migration if no old config exists
        if (!oldFile.exists()) {
            Debugging.log("Migration", "No config.yml exists. Skipping migration.");
            return true;
        }

        // Load old config
        YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(oldFile);

        // Load default config from plugin jar
        YamlConfiguration defaultConfig;
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

        // Merge old config values into default config
        mergeConfigs(oldConfig, defaultConfig, "");

        // Update config-version to latest installed version
        defaultConfig.set("config-version", Versioning.installedVersion);

        // Backup old config and replace
        File backup = new File(dataFolder, "config.yml.old");
        oldFile.renameTo(backup);

        try {
            defaultConfig.save(oldFile);
        } catch (IOException e) {
            Debugging.log("Migration", "Failed to save migrated config: " + e.getMessage());
            return false;
        }

        Debugging.log("Migration", "config.yml successfully migrated and backed up as config.yml.old");
        return true;
    }

    /**
    * Recursively merges old configuration values into the new configuration.
    * Nested keys are handled properly to avoid MemorySection serialization.
    *
    * @param oldCfg The old configuration file
    * @param newCfg The new configuration file (from plugin jar)
    * @param path   Current YAML path for recursion ("" for root)
    */
    private static void mergeConfigs(YamlConfiguration oldCfg, YamlConfiguration newCfg, String path) {
        ConfigurationSection newSection = path.isEmpty() ? newCfg : newCfg.getConfigurationSection(path);
        if (newSection == null)
            return;

        for (String key : newSection.getKeys(false)) {
            String fullPath = path.isEmpty() ? key : path + "." + key;

            if (newSection.isConfigurationSection(key)) {
                mergeConfigs(oldCfg, newCfg, fullPath);
            } else if (oldCfg.contains(fullPath)) {
                Object oldValue = oldCfg.get(fullPath);
                newCfg.set(fullPath, oldValue);
            }
        }
    }
}
