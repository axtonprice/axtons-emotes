package com.arizonsoftware.axtonsemotes.utils;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Configuration {

   public static final String CONFIG_FILE_NAME = "config.yml";
   public static final String EMOTES_FILE_NAME = "emotes.yml";
   private static final AxtonsEmotes instance = AxtonsEmotes.getInstance();
   private static final String className = Configuration.class.getSimpleName();
   private static final ConcurrentHashMap<String, YamlConfiguration> configCache = new ConcurrentHashMap<>();

   public static final String[] languages = { "es.yml", "fr.yml", "it.yml", "ru.yml", "de.yml", "pl.yml", "zh.yml",
         "uk.yml", "pa.yml", "sv.yml", "nl.yml" };

   private Configuration() {
      throw new IllegalStateException("Utility class");
   }

   /**
    * Sets up default directories and configuration files.
    */
   public static void saveDefaultConfigs() {
      try {
         // Create necessary directories
         createDirectory(instance.getDataFolder().toPath());
         createDirectory(instance.getDataFolder().toPath().resolve("lang"));
         createDirectory(instance.getDataFolder().toPath().resolve("lang/translations"));

         // Create config.yml if missing
         saveDefaultConfigFile(CONFIG_FILE_NAME, false);

         // Create emotes.yml if missing
         saveDefaultConfigFile(EMOTES_FILE_NAME, false);

         // Create language files if missing
         saveDefaultConfigFile("lang/en.yml", false);
         Arrays.stream(languages)
               .forEach(langFile -> saveDefaultConfigFile("lang/translations/" + langFile, false));
      } catch (Exception e) {
         Debugging.logError("Configuration setup failed", e);
      }
   }


   /**
    * Validates the main configuration files for the plugin.
    * This ensures that all necessary configuration files exist and are properly formatted
    * before the plugin continues execution.
    */
   public static void validateConfig() {

      // Save default configs if missing
      Configuration.saveDefaultConfigs();

      // Fetch config file
      YamlConfiguration configYML = getConfig(CONFIG_FILE_NAME);
      File configFile = new File(instance.getDataFolder(), CONFIG_FILE_NAME);

      // Validate language config
      ensureLangConfig(configYML, configFile);
   }

   /**
    * Creates a directory if it does not exist.
    *
    * @param path The path to create.
    */
   public static void createDirectory(Path path) {
      try {
         if (!Files.exists(path, new LinkOption[0])) {
            Files.createDirectories(path);
            Debugging.log(className + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
                  "Created directory: " + path.getFileName());
         }
      } catch (IOException e) {
         Debugging.logError("Failed to create directory: " + path, e);
      }
   }

   /**
    * Saves a default resource file to the plugin folder.
    *
    * @param fileName The file name to save.
    * @param replace  Whether to overwrite existing file.
    */
   public static void saveDefaultConfigFile(String fileName, boolean replace) {
      File file = new File(instance.getDataFolder(), fileName);
      if (!file.exists() || replace) {
         try {
            instance.saveResource(fileName, replace);
            ++Registry.CONFIGS_COUNT;
            Debugging.log(className + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
                  "Created default configuration: " + fileName);
            // reload config after saving
            reloadConfig(fileName);
         } catch (Exception e) {
            Debugging.logError("Failed to save default configuration: " + fileName, e);
         }
      }
   }

   /**
    * Returns a cached YamlConfiguration object for the given file.
    *
    * @param configFile File name of the config.
    * @return The loaded YamlConfiguration.
    */
   public static YamlConfiguration getConfig(String configFile) {
      return configCache.computeIfAbsent(configFile, file -> {
         try {
            File configPath = new File(instance.getDataFolder(), file);
            return !configPath.exists() ? new YamlConfiguration() : YamlConfiguration.loadConfiguration(configPath);
         } catch (Exception e) {
            instance.getLogger().severe("Failed to load configuration: " + file + ": " + e.getMessage());
            return new YamlConfiguration();
         }
      });
   }

   /**
    * Reloads a configuration file and updates cache.
    *
    * @param configFile File name to reload.
    */
   public static void reloadConfig(String configFile) {
      configCache.remove(configFile);
      getConfig(configFile);
   }

   /**
    * Returns a string value from a config file.
    *
    * @param configFile File name.
    * @param key        Config key.
    * @return Config value or null.
    */
   public static String getString(String configFile, String key) {
      YamlConfiguration config = getConfig(configFile);
      return config != null ? config.getString(key) : null;
   }

   /**
    * Returns a string value from a config file with default.
    *
    * @param configFile   File name.
    * @param key          Config key.
    * @param defaultValue Default value.
    * @return Config value or default.
    */
   public static String getString(String configFile, String key, String defaultValue) {
      YamlConfiguration config = getConfig(configFile);
      return config != null ? config.getString(key, defaultValue) : defaultValue;
   }

   /**
    * Returns a boolean value from a config file.
    *
    * @param configFile File name.
    * @param key        Config key.
    * @return Config boolean value.
    */
   public static boolean getBoolean(String configFile, String key) {
      YamlConfiguration config = getConfig(configFile);
      return config != null && config.getBoolean(key);
   }

   /**
    * Returns a boolean value from a config file with default.
    *
    * @param configFile   File name.
    * @param key          Config key.
    * @param defaultValue Default value.
    * @return Config boolean value or default.
    */
   public static boolean getBoolean(String configFile, String key, boolean defaultValue) {
      YamlConfiguration config = getConfig(configFile);
      return config != null ? config.getBoolean(key, defaultValue) : defaultValue;
   }

   /**
    * Returns an integer value from a config file.
    *
    * @param configFile File name.
    * @param key        Config key.
    * @return Config integer value.
    */
   public static int getInt(String configFile, String key) {
      YamlConfiguration config = getConfig(configFile);
      return config != null ? config.getInt(key) : 0;
   }

   /**
    * Returns an integer value from a config file with default.
    *
    * @param configFile   File name.
    * @param key          Config key.
    * @param defaultValue Default value.
    * @return Config integer value or default.
    */
   public static int getInt(String configFile, String key, int defaultValue) {
      YamlConfiguration config = getConfig(configFile);
      return config != null ? config.getInt(key, defaultValue) : defaultValue;
   }

   /**
    * Ensures language config file exists and sets default if missing.
    *
    * @param configYML   Loaded configuration.
    * @param configFile  File to save if needed.
    */
   private static void ensureLangConfig(YamlConfiguration configYML, File configFile) {

      // Get language files and config
      String language = configYML.getString("language", "en");
      File languageFile = new File(instance.getDataFolder(), "lang/en.yml");

      // Language file override if not English
      if (!language.equalsIgnoreCase("en")) {
         languageFile = new File(instance.getDataFolder(), "lang/translations/" + language + ".yml");
      }

      // If language file missing, reset to English
      if (!languageFile.exists()) {

         // Override to English
         language = "en";

         // Reset language key in config to English
         configYML.set("language", language);

         // Save English language file
         saveDefaultConfigFile("lang/en.yml", false);
         // saveConfiguration(configYML, configFile, MessageHandler.get("config.error.saving"));

         // Debug logging
         Debugging.raw("warning", MessageHandler.get("plugin.startup.configuration.error.invalid_lang"));
         Debugging.logToFile(className + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
               MessageHandler.get("plugin.startup.configuration.error.invalid_lang"));
      }
   }

   /**
    * Returns the keys of a configuration section.
    *
    * @param configFile  File name.
    * @param sectionPath Path of the section.
    * @return Set of keys or empty set if invalid.
    */
   public static Set<String> getConfigSectionKeys(String configFile, String sectionPath) {
      try {
         YamlConfiguration config = getConfig(configFile);
         if (config != null && config.isConfigurationSection(sectionPath)) {
            return config.getConfigurationSection(sectionPath).getKeys(false);
         }
      } catch (Exception e) {
         instance.getLogger().warning("Error getting configuration section keys from " + configFile +
               " at path " + sectionPath + ": " + e.getMessage());
      }
      return Set.of();
   }
}
