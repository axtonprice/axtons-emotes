package com.arizonsoftware.axtonsemotes.utils;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class MessageHandler {

   private static final String DEFAULT_LANGUAGE = "en";
   private static final String MISSING_KEY_PLACEHOLDER = "<NO_LANG_KEY>";

   private static volatile String cachedLanguage = null;
   private static final Map<String, String> messageCache = new ConcurrentHashMap<>();

   private MessageHandler() {
      throw new IllegalStateException("Utility class");
   }

   /**
    * Returns a message by key with color codes parsed.
    */
   public static String get(String key) {
      return get(key, true);
   }

   /**
    * Returns a message by key with optional color code parsing.
    */
   public static String get(String key, boolean parseColors) {
      String message = getCachedMessage(key);
      return parseColors ? parseColor(message) : message;
   }

   /**
    * Returns a message by key and replaces placeholders.
    */
   public static String get(String key, Map<String, String> placeholders) {
      return get(key, placeholders, true);
   }

   /**
    * Returns a message by key, replaces placeholders, with optional color parsing.
    */
   public static String get(String key, Map<String, String> placeholders, boolean parseColors) {
      String message = getCachedMessage(key);
      message = replacePlaceholders(message, placeholders);
      return parseColors ? parseColor(message) : message;
   }

   /**
    * Returns a message by key and replaces placeholders using arrays.
    */
   public static String get(String key, String[] placeholderNames, String[] replacementValues) {
      return format(get(key), placeholderNames, replacementValues);
   }

   /**
    * Replaces placeholders in a message using arrays.
    */
   public static String format(String message, String[] placeholderNames, String[] replacementValues) {
      if (placeholderNames.length != replacementValues.length) {
         getLogger().warning("Placeholder names and values arrays have different lengths for message: " + message);
         return parseColor(message);
      }
      String result = message;
      for (int i = 0; i < placeholderNames.length; i++) {
         if (replacementValues[i] != null) {
            result = result.replace("%" + placeholderNames[i] + "%", replacementValues[i]);
         } else {
            getLogger().warning("Null replacement value for placeholder: " + placeholderNames[i]);
         }
      }
      return parseColor(result);
   }

   /**
    * Parses color codes in a string.
    */
   public static String parseColor(String message) {
      return message == null ? "" : message.replace('&', '§');
   }

   /**
    * Clears the message cache and language cache.
    */
   public static void clearCache() {
      messageCache.clear();
      cachedLanguage = null;
   }

   /**
    * Reloads the message system and language cache.
    */
   public static void reload() {
      clearCache();
      getConfiguredLanguage();
   }

   /**
    * Formats a message as a success message.
    */
   public static String parseSuccess(String key) {
      return parseColor("&8[&e&lAE&r&8]&r &a" + get(key, false));
   }

   /**
    * Formats a message as a warning message.
    */
   public static String parseWarning(String key) {
      return parseColor("&8[&6&l!&r&8]&r &7" + get(key, false));
   }

   /**
    * Formats a message as an error message.
    */
   public static String parseError(String key) {
      return parseColor("&8[&c&l!&r&8]&r &7" + get(key, false));
   }

   /**
    * Formats a message as an info message.
    */
   public static String parseInfo(String key) {
      return parseColor("&8[&b&l?&r&8]&r &7" + get(key, false));
   }

   /**
    * Internal helper for custom formatting with placeholders.
    */
   private static String parseCustom(String prefix, String textColor, String key, String[] placeholderNames,
         String[] replacementValues) {
      return parseColor(prefix + " " + textColor + format(get(key, false), placeholderNames, replacementValues));
   }

   public static String parseDefaultReplace(String key, String[] placeholderNames, String[] replacementValues) {
      return parseCustom("&8[&e&lAE&r&8]&r", "&7", key, placeholderNames, replacementValues);
   }

   public static String parseWarningReplace(String key, String[] placeholderNames, String[] replacementValues) {
      return parseCustom("&8[&6&l!&r&8]&r", "&7", key, placeholderNames, replacementValues);
   }

   public static String parseErrorReplace(String key, String[] placeholderNames, String[] replacementValues) {
      return parseCustom("&8[&c&l!&r&8]&r", "&7", key, placeholderNames, replacementValues);
   }

   public static String parseInfoReplace(String key, String[] placeholderNames, String[] replacementValues) {
      return parseCustom("&8[&b&l?&r&8]&r", "&7", key, placeholderNames, replacementValues);
   }

   // no prefix
   public static String parseReplace(String key, String[] placeholderNames,
         String[] replacementValues) {
      return format(get(key, false), placeholderNames, replacementValues);
   }

   /**
    * Returns a cached message or loads it if missing.
    */
   private static String getCachedMessage(String key) {
      return messageCache.computeIfAbsent(key, MessageHandler::loadMessage);
   }

   /**
    * Loads a message from the language file, with fallback to English.
    */
   private static String loadMessage(String key) {
      String configuredLanguage = getConfiguredLanguage();
      String langFileToUse;

      // Lang.yml if english, else translations/<language>.yml
      if (configuredLanguage.equals(DEFAULT_LANGUAGE)) {
         langFileToUse = "lang/en.yml";
      } else {
         langFileToUse = "lang/translations/" + configuredLanguage + ".yml";
      }

      // Try to get message from selected language
      String message = Configuration.getString(langFileToUse, key);
      if (message != null) {
         return message;
      }

      // Fallback to default language if not found
      if (!DEFAULT_LANGUAGE.equals(configuredLanguage)) {
         message = Configuration.getString(langFileToUse, key);
         if (message != null) {
            logMissingKey(key, configuredLanguage, false);
            return message;
         }
      }

      logMissingKey(key, configuredLanguage, true);
      return MISSING_KEY_PLACEHOLDER + ":" + key;
   }

   /**
    * Returns the current language from config, cached for performance.
    */
   private static String getConfiguredLanguage() {
      if (cachedLanguage == null) {
         synchronized (MessageHandler.class) {
            if (cachedLanguage == null) {
               String language = Configuration.getString("config.yml", "language", DEFAULT_LANGUAGE);
               cachedLanguage = (language != null && !language.trim().isEmpty()) ? language.trim() : DEFAULT_LANGUAGE;
            }
         }
      }
      return cachedLanguage;
   }

   /**
    * Reloads the language files by clearing the cached language and message cache.
    * This method should be called when language files are updated or reloaded to ensure
    * that the latest language data is used throughout the application.
    */
   public static void reloadLanguageFiles() {
      cachedLanguage = null;
      messageCache.clear();
   }

   /**
    * Replaces placeholders in a message with provided values.
    */
   private static String replacePlaceholders(String message, Map<String, String> placeholders) {
      if (placeholders == null || placeholders.isEmpty()) {
         return message;
      }
      String result = message;
      for (Entry<String, String> entry : placeholders.entrySet()) {
         if (entry.getValue() != null) {
            result = result.replace("%" + entry.getKey() + "%", entry.getValue());
         }
      }
      return result;
   }

   /**
    * Logs a missing language key with info or warning level.
    */
   private static void logMissingKey(String key, String language, boolean totallyMissing) {
      if (totallyMissing) {
         getLogger().warning(
               String.format("Missing language key '%s' in both %s.yml and %s.yml", key, language, DEFAULT_LANGUAGE));
      } else {
         getLogger().info(String.format("Using fallback for key '%s' (missing in %s.yml)", key, language));
      }
   }

   /**
    * Returns the plugin logger, or a fallback logger if unavailable.
    */
   private static Logger getLogger() {
      try {
         return AxtonsEmotes.getInstance().getLogger();
      } catch (Exception e) {
         return Logger.getLogger("AxtonsEmotes");
      }
   }
}
