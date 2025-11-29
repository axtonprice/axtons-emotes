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

   private static final AxtonsEmotes instance = AxtonsEmotes.getInstance();
   private static final String className = Configuration.class.getSimpleName();
   private static final String LANG_FOLDER = "lang";
   private static final String CONFIG_FILE = "config.yml";
   private static final String EMOTES_FILE = "emotes.yml";
   private static final ConcurrentHashMap<String, YamlConfiguration> configCache = new ConcurrentHashMap<>();

   private Configuration() {
      throw new IllegalStateException("Utility class");
   }

   /**
    * Sets up default directories and configuration files.
    */
   public static void ensureDefaultConfigs() {
      try {
         // Create necessary directories
         createDirectory(instance.getDataFolder().toPath());
         createDirectory(instance.getDataFolder().toPath().resolve("lang"));
         createDirectory(instance.getDataFolder().toPath().resolve("lang/translations"));

         // Create config.yml if missing
         saveDefaultConfigFile(CONFIG_FILE, false);

         // Create emotes.yml if missing
         saveDefaultConfigFile(EMOTES_FILE, false);

         // Create language files if missing
         saveDefaultConfigFile("lang/en.yml", false);
         Arrays.stream(new String[] { "es.yml", "fr.yml", "it.yml", "ru.yml", "de.yml", "pl.yml" })
               .forEach(langFile -> saveDefaultConfigFile("lang/translations/" + langFile, false));

         // Log completion
         Debugging.log(className + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
               "Configuration setup completed successfully");
      } catch (Exception e) {
         Debugging.logError("Configuration setup failed", e);
      }
   }

   /**
   * Validates system config for language and prefix formatting.
   */
   public static void validateConfig() {

      // Fetch config file
      YamlConfiguration configYML = getConfig(CONFIG_FILE);
      File configFile = new File(instance.getDataFolder(), CONFIG_FILE);

      // Create config file if missing
      if (!configFile.exists()) {
         saveDefaultConfigFile(CONFIG_FILE, false);
         reloadConfig(CONFIG_FILE);
         configYML = getConfig(CONFIG_FILE);
      }

      // Validate language config
      validateLanguageConfig(configYML, configFile);

      // Ensure prefix ends with space
      String prefix = configYML.getString("prefix", "").trim();
      if (!prefix.isEmpty() && !prefix.endsWith(" ")) {
         configYML.set("prefix", prefix + " ");
         saveConfiguration(configYML, configFile, MessageHandler.get("config.error.saving"));
      }
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
                  "Saved default configuration: " + fileName);
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
    * Deletes and resets a configuration file.
    *
    * @param configFile File name to reset.
    */
   public static void resetConfig(String configFile) {
      File file = new File(instance.getDataFolder(), configFile);
      if (file.exists()) {
         try {
            Files.delete(file.toPath());
            Debugging.log(className + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
                  "Deleted configuration file: " + configFile);
         } catch (IOException e) {
            Debugging.logError("Failed to delete configuration file: " + configFile, e);
         }
      }
      saveDefaultConfigFile(configFile, true);
      reloadConfig(configFile);
   }

   /**
    * Ensures language config file exists and sets default if missing.
    *
    * @param configYML   Loaded configuration.
    * @param configFile  File to save if needed.
    */
   private static void validateLanguageConfig(YamlConfiguration configYML, File configFile) {
      String language = configYML.getString("language", "en");
      File languageFile = new File(instance.getDataFolder(), LANG_FOLDER + File.separator + language + ".yml");
      if (!languageFile.exists()) {
         language = "en";
         configYML.set("language", language);
         saveDefaultConfigFile(LANG_FOLDER + "/" + language + ".yml", false);
         saveConfiguration(configYML, configFile, MessageHandler.get("config.error.saving"));
         Debugging.raw("warning", MessageHandler.get("plugin.startup.configuration.error.invalid_lang"));
         Debugging.logToFile(className + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
               MessageHandler.get("plugin.startup.configuration.error.invalid_lang"));
      }
   }

   /**
    * Saves a YamlConfiguration to disk.
    *
    * @param configYML   Configuration to save.
    * @param configFile  File to write.
    * @param errorMessage Error message for logging.
    */
   private static void saveConfiguration(YamlConfiguration configYML, File configFile, String errorMessage) {
      try {
         configYML.save(configFile);
      } catch (Exception e) {
         Debugging.logError("Failed to save configuration: " + errorMessage, e);
      }
   }

   /**
    * Returns the keys of a configuration section.
    *
    * @param configFile  File name.
    * @param sectionPath Path of the section.
    * @return Set of keys or empty set if invalid.
    */
   public static Set<String> getConfigurationSectionKeys(String configFile, String sectionPath) {
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
