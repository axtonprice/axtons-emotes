package com.arizonsoftware.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;

import com.arizonsoftware.Main;

public class Debugging {

    private static File logFile = new File(Main.getInstance().getDataFolder(), Configuration.getString("config.yml", "debug-mode.log-file"));
    private static String timestamp = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS")) + "]";

    /**
     * Logs a debug message with the specified source identifier
     * and log contents.
     * 
     * @param sourceIdentifier The identifier of the source
     *                         where the log message originated
     *                         from.
     * @param logContents      The contents of the log message.
     */
    public static void log(String sourceIdentifier, String logContents) {
        if (Configuration.getBoolean("config.yml", "debug-mode.enabled")) {
            String formattedLog = "[DEBUG/" + sourceIdentifier + "/] " + logContents;
            Bukkit.getLogger().warning(formattedLog);

            if (Configuration.getBoolean("config.yml", "debug-mode.log-to-file") && Configuration.getString("config.yml", "debug-mode.log-file") != null) {
                createDebugFile();
                try (FileWriter writer = new FileWriter(logFile, true)) {
                    writer.write(timestamp + " " + formattedLog + "\n");
                } catch (IOException e) {
                    Bukkit.getLogger().severe("[DEBUG/" + Debugging.class.getSimpleName() + "/log] Could not write to debug log file: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Logs the specified contents to a debug log file.
     * 
     * @param sourceIdentifier the identifier of the source
     *                         logging the contents
     * @param logContents      the contents to be logged
     */
    public static void logToFile(String sourceIdentifier, String logContents) {
        if (Configuration.getBoolean("config.yml", "debug-mode.enabled") && Configuration.getBoolean("config.yml", "debug-mode.log-to-file") && Configuration.getString("config.yml", "debug-mode.log-file") != null) {
            createDebugFile();
            try (FileWriter writer = new FileWriter(logFile, true)) {
                writer.write(timestamp + " [DEBUG/" + sourceIdentifier + "/] " + logContents + "\n");
            } catch (IOException e) {
                Bukkit.getLogger().severe("[DEBUG/" + Debugging.class.getSimpleName() + "/logToFile] Could not write to debug log file: " + e.getMessage());
            }
        }
    }

    /**
     * Creates a debug log file if it does not exist.
     * 
     * @throws IllegalStateException if initialize() method has
     *                               not been called before
     *                               logging.
     * @throws IOException           if an error occurs while
     *                               creating the debug log
     *                               file.
     */
    public static void createDebugFile() {
        if (logFile == null) {
            Bukkit.getLogger().severe("[DEBUG/" + Debugging.class.getSimpleName() + "/createDebugFile] Could not create debug log file: initialize() must be called before logging.");
            return;
        }

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (Exception e) {
                Bukkit.getLogger().severe("[DEBUG/" + Debugging.class.getSimpleName() + "/createDebugFile] Could not create debug log file: " + e.getMessage());
            }
        }
    }
}