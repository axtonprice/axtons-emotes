package com.arizonsoftware.utils;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import com.arizonsoftware.Main;

public class Configuration {

    private static Main instance = Main.getInstance();
    public static YamlConfiguration config = getConfig("config.yml");

    /**
     * Sets up the configuration for the application. This
     * method creates necessary directories and initializes
     * default settings. It also migrates the configuration if
     * needed.
     * 
     * @throws Exception if an error occurs during the setup
     *                   process.
     */
    public static void setupConfig() {
        try {
            if (!instance.getDataFolder().exists()) {
                instance.getDataFolder().mkdir();
            }

            if (!new File(instance.getDataFolder(), "lang").exists()) {
                new File(instance.getDataFolder(), "lang").mkdir();
            }

            saveDefaults();
            migrateConfig();
        } catch (Exception e) {
            Debugging.log(Configuration.class.getSimpleName(), "Error setting up configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves the default configuration files. This method saves
     * the default configuration files for the application, as
     * well as the language data.
     */
    public static void saveDefaults() {
        saveDefaultConfigFile("config.yml");
        saveDefaultConfigFile("commands.yml");
        saveDefaultConfigFile("lang/en.yml");
        saveDefaultConfigFile("lang/fr.yml");
        saveDefaultConfigFile("lang/es.yml");
        saveDefaultConfigFile("lang/it.yml");
        saveDefaultConfigFile("lang/ru.yml");
    }

    /**
     * Saves the default configuration file with the specified
     * file name. If the file does not exist, it is saved from
     * the resources. Increases the count of saved
     * configurations in the registry. Logs the saved
     * configuration file name for debugging purposes.
     * 
     * @param fileName the name of the configuration file to be
     *                 saved
     */
    private static void saveDefaultConfigFile(String fileName) {
        try {
            File file = new File(instance.getDataFolder(), fileName);
            if (!file.exists()) {
                instance.saveResource(fileName, false);
                Registry.configsCount++;
                Debugging.log(Configuration.class.getSimpleName(), "Saved configuration file: " + file.getName());
            }
        } catch (Exception e) {
            Debugging.log(Configuration.class.getSimpleName(), "Error saving default config file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the YAML configuration file specified by the
     * given configuration path.
     *
     * @param configuration The path of the configuration file.
     * @return The loaded YamlConfiguration object.
     */
    private static YamlConfiguration getConfig(String configuration) {
        return YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), configuration));
    }

    /**
     * Retrieves a string value from the specified configuration
     * using the given key.
     *
     * @param configuration the configuration file path
     * @param key           the key to retrieve the string value
     * @return the string value associated with the given key
     */
    public static String getString(String configuration, String key) {
        config = getConfig(configuration);
        return config.getString(key);
    }

    /**
     * Retrieves a boolean value from the specified
     * configuration using the given key.
     *
     * @param configuration the configuration file path or name
     * @param key           the key to retrieve the boolean
     *                      value
     * @return the boolean value associated with the key
     */
    public static Boolean getBoolean(String configuration, String key) {
        config = getConfig(configuration);
        return config.getBoolean(key);
    }

    /**
     * Migrates the configuration if needed. If certain
     * properties are missing or the installed version differs
     * from the configured version, it renames the existing
     * config file, saves the default one, and logs the
     * migration process and errors.
     */
    public static void migrateConfig() {
        try {
            // Reload config to get latest values
            config = getConfig("config.yml");
            
            String installedVersion = Versioning.getCurrent();
            String configuredVersion = Versioning.getConfigured();
            
            // Required configuration keys that must be present
            String[] requiredKeys = {
                "language",
                "allow-self-executions",
                "allow-list-commands",
                "debug-mode.enabled",
                "debug-mode.log-to-file",
                "debug-mode.log-file",
                "check-for-latest",
                "notify-on-update"
            };
            
            // Check if configuration is missing any required fields
            boolean missingFields = false;
            for (String key : requiredKeys) {
                if (!config.contains(key)) {
                    missingFields = true;
                    break;
                }
            }
            
            if (missingFields) {
                Debugging.log(Configuration.class.getSimpleName() + "/migrateConfig", "Configuration is missing required fields, replacing with default");
                instance.saveResource("config.yml", true);
                return;
            }

            // Check if version migration is needed
            if (installedVersion == null || configuredVersion == null) {
                Debugging.log(Configuration.class.getSimpleName() + "/migrateConfig", "Unable to determine version information");
                return;
            }

            if (!installedVersion.equals(configuredVersion)) {
                Debugging.log(Configuration.class.getSimpleName() + "/migrateConfig", "Installed: v" + installedVersion);
                Debugging.log(Configuration.class.getSimpleName() + "/migrateConfig", "Configured: v" + configuredVersion);
                Debugging.log(Configuration.class.getSimpleName() + "/migrateConfig", "Migrating configuration v" + configuredVersion + "...v" + installedVersion);

                File oldConfig = new File(instance.getDataFolder(), "config.old.yml");
                File newConfig = new File(instance.getDataFolder(), "config.yml");

                if (oldConfig.exists()) {
                    oldConfig.delete();
                }
                newConfig.renameTo(oldConfig);

                instance.saveResource("config.yml", true);

                Debugging.log(Configuration.class.getSimpleName() + "/migrateConfig", "Configuration v" + configuredVersion + " migrated to v" + installedVersion);
            }
        } catch (Exception e) {
            Debugging.log(Configuration.class.getSimpleName(), "Error migrating configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}