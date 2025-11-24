package com.arizonsoftware.axtonsemotes.utils;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Debugging {

   private static final String CONFIG_FILE = "config.yml";
   private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

   private static volatile YamlConfiguration configYML;
   private static volatile File logFile;
   private static long startTime;

   private Debugging() {
      throw new IllegalStateException("Utility class");
   }

   /**
    * Starts a timer for measuring execution time.
    */
   public static void startTimer() {
      startTime = System.currentTimeMillis();
   }

   /**
    * Ends the timer and returns the elapsed time.
    *
    * @return The time elapsed since {@link #startTimer()} was called, in milliseconds.
    */
   public static long endTimer() {
      return System.currentTimeMillis() - startTime;
   }

   /**
    * Logs an error message and prints stack trace if debug is enabled.
    *
    * @param message The error message to log.
    * @param e       The exception to log.
    */
   public static void logError(String message, Exception e) {
      String errorMessage = String.format("%s: %s", message, e.getMessage());
      raw("severe", errorMessage);
      YamlConfiguration config = getConfigSafely();
      if (config != null && config.getBoolean("debug-mode.enabled", false)) {
         e.printStackTrace();
      }
   }

   /**
    * Returns the current timestamp formatted for logging.
    *
    * @return The formatted timestamp string.
    */
   private static String getTimestamp() {
      return "[" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + "]";
   }

   /**
    * Logs a raw message to console depending on type.
    *
    * @param logType     The type of log: "info", "warning", "severe", "message".
    * @param logContents The message content.
    */
   public static void raw(String logType, String logContents) {
      String pluginName = AxtonsEmotes.getInstance().getName();
      String formattedMessage = String.format("[%s] %s", pluginName, logContents);

      switch (logType) {
         case "info" -> raw("message", formattedMessage);
         case "warning" -> Bukkit.getLogger().warning(formattedMessage);
         case "severe" -> Bukkit.getLogger().severe(formattedMessage);
         case "message" -> Bukkit.getServer().getConsoleSender().sendMessage(formattedMessage);
         default -> raw("message", formattedMessage);
      }
   }

   /**
    * Logs a debug message to console and optionally writes to the debug file.
    *
    * @param sourceIdentifier Identifier of the class or method logging the message.
    * @param logContents      The debug message.
    */
   public static void log(String sourceIdentifier, String logContents) {
      YamlConfiguration config = getConfigSafely();
      if (config != null && config.getBoolean("debug-mode.enabled", false)) {
         String formattedLog = "[AxtonsEmotes/DEBUG] [" + sourceIdentifier + "] " + logContents;
         Bukkit.getLogger().warning(formattedLog);

         if (!"none".equals(config.getString("debug-mode.file-name"))) {
            createDebugFile();
            File debugFile = getLogFileSafely();
            if (debugFile != null)
               writeToFile(debugFile, formattedLog);
         }
      }
   }

   /**
    * Logs a debug message only to the debug file if logging is enabled.
    *
    * @param sourceIdentifier Identifier of the class or method logging the message.
    * @param logContents      The debug message.
    */
   public static void logToFile(String sourceIdentifier, String logContents) {
      YamlConfiguration config = getConfigSafely();
      if (config != null && config.getBoolean("debug-mode.enabled", false)
            && config.getBoolean("debug-mode.log-to-file", false)
            && config.getString("debug-mode.file-name") != null) {

         createDebugFile();
         File debugFile = getLogFileSafely();
         if (debugFile != null) {
            writeToFile(debugFile, "[AxtonsEmotes/DEBUG] [" + sourceIdentifier + "] " + logContents);
         }
      }
   }

   /**
    * Creates the debug log file if it does not exist.
    */
   public static void createDebugFile() {
      File debugFile = getLogFileSafely();
      if (debugFile != null && !debugFile.exists()) {
         try {
            if (!debugFile.createNewFile()) {
               raw("warning", "Debug log file already exists: " + debugFile.getName());
            }
         } catch (Exception e) {
            raw("severe", "Could not create debug log file: " + e.getMessage());
         }
      }
   }

   /**
    * Reloads the debug configuration and resets cached file references.
    */
   public static void reloadConfig() {
      synchronized (Debugging.class) {
         configYML = null;
         logFile = null;
      }
      getConfigSafely();
      getLogFileSafely();
   }

   /**
    * Retrieves the debug configuration, initializing if necessary.
    *
    * @return The debug configuration YAML, or null if it failed to load.
    */
   private static YamlConfiguration getConfigSafely() {
      if (configYML == null) {
         synchronized (Debugging.class) {
            if (configYML == null) {
               try {
                  configYML = Configuration.getConfig(CONFIG_FILE);
               } catch (Exception e) {
                  Bukkit.getLogger().severe("[AxtonsEmotes] Failed to load debug configuration: " + e.getMessage());
                  return null;
               }
            }
         }
      }
      return configYML;
   }

   /**
    * Retrieves the debug log file, initializing if necessary.
    *
    * @return The debug log file, or null if it could not be obtained.
    */
   private static File getLogFileSafely() {
      if (logFile == null) {
         synchronized (Debugging.class) {
            if (logFile == null) {
               try {
                  String fileName = Configuration.getString(CONFIG_FILE, "debug-mode.file-name");
                  if (fileName != null)
                     logFile = new File(AxtonsEmotes.getInstance().getDataFolder(), fileName);
               } catch (Exception e) {
                  Bukkit.getLogger().severe("[AxtonsEmotes] Failed to get debug log file: " + e.getMessage());
                  return null;
               }
            }
         }
      }
      return logFile;
   }

   /**
    * Writes a debug message to the specified file with timestamp.
    *
    * @param file    The file to write to.
    * @param message The message content.
    */
   private static void writeToFile(File file, String message) {
      try (FileWriter writer = new FileWriter(file, true)) {
         writer.write(getTimestamp() + " " + message + "\n");
      } catch (IOException e) {
         raw("severe", "Could not write to debug log file: " + e.getMessage());
      }
   }
}
