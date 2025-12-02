/**
 * Copyright (C) 2025 axtonprice
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.arizonsoftware.axtonsemotes;

import com.arizonsoftware.axtonsemotes.utils.Configuration;
import com.arizonsoftware.axtonsemotes.utils.Debugging;
import com.arizonsoftware.axtonsemotes.utils.MessageHandler;
import com.arizonsoftware.axtonsemotes.utils.Metrics;
import com.arizonsoftware.axtonsemotes.utils.Metrics.DrilldownPie;
import com.arizonsoftware.axtonsemotes.utils.Metrics.SimplePie;
import com.arizonsoftware.axtonsemotes.utils.Registry;
import com.arizonsoftware.axtonsemotes.utils.Versioning;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AxtonsEmotes extends JavaPlugin {
   public static long startTime; // Startup timings

   /**
    * Called when the plugin is loaded.
    * Initializes and sets up the configuration for the plugin.
    */
   public void onLoad() {
      // Setup configuration
      Configuration.saveDefaultConfigs();
   }

   /**
    * Called when the plugin is enabled.
    *
    * Initializes the plugin by recording the start time, logging startup messages,
    * checking the plugin version, validating system configuration, registering all
    * necessary components, and starting metrics collection. Outputs startup progress
    * and completion messages to the debug log.
    */
   public void onEnable() {
      startTime = System.currentTimeMillis();

      // Startup messages
      Debugging.raw("message", "---------------------------------");
      if (Bukkit.getLogger().isLoggable(Level.INFO)) {
         String pluginName = getInstance().getDescription().getName();
         String pluginVersion = getInstance().getDescription().getVersion();
         String message = String.format("[-] %s %s v%s", MessageHandler.get("plugin.startup.progress_suffix.starting"),
               pluginName, pluginVersion);
         Debugging.raw("message", message);
      }

      // Validate configuration
      Debugging.log("startup", MessageHandler.get("plugin.startup.configuration.start"));
      Configuration.validateConfig();

      // Check if debug mode is enabled and alert
      if (Configuration.getBoolean("config.yml", "debug-mode.enabled")) {
         Debugging.log("startup",
               "Debug mode is enabled - you will see additional console messages.");
      }

      // Check for updates
      long updateCheckDelay = 12 * 60 * 60 * 20L; // 12 hours in ticks
      Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
         Versioning.checkForUpdates();
      }, 0, updateCheckDelay);

      // Register components
      Debugging.log("startup", MessageHandler.get("plugin.startup.registry.start"));
      Registry.registerAll(startTime);

      // Start BStats metrics
      if (Configuration.getBoolean("config.yml", "enable-metrics")) {
         Debugging.log("startup",
               MessageHandler.get("plugin.startup.bstats.start"));
         initMetrics();
      }

      // Startup message - footer
      String footer = MessageHandler.format(MessageHandler.get("plugin.startup.footer"),
            new String[] { "progress_suffix" }, new String[] { "Loaded" });
      Debugging.raw("message", "[-] " + footer + " " + (System.currentTimeMillis() - startTime) + "ms!");
      Debugging.raw("message", "---------------------------------");

   }

   /**
    * Initializes plugin metrics for usage statistics reporting.
    * Metrics are reported using the bStats Metrics library.
    */
   private void initMetrics() {
      // Initialize metrics
      Metrics metrics = new Metrics(this, 23323);

      // Plugin version chart
      metrics.addCustomChart(new DrilldownPie("plugin_version", () -> {
         Map<String, Map<String, Integer>> map = new HashMap<>();
         String installedVersion = Versioning.installedVersion;
         Map<String, Integer> entry = new HashMap<>();
         entry.put(installedVersion, 1);
         map.put("Axton's Emotes", entry);
         return map;
      }));

      // Language chart
      metrics.addCustomChart(new SimplePie("language", () -> {
         return Configuration.getString("config.yml", "language", "en");
      }));
   }

   /**
    * Returns the instance of the {@link AxtonsEmotes} plugin.
    * 
    * This method retrieves the plugin instance using Bukkit's {@code getPlugin} method.
    * It is commonly used to access plugin-wide resources and configuration.
    *
    * @return the instance of {@link AxtonsEmotes}
    */
   public static AxtonsEmotes getInstance() {
      return (AxtonsEmotes) getPlugin(AxtonsEmotes.class);
   }
}
