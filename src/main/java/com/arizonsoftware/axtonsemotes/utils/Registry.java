package com.arizonsoftware.axtonsemotes.utils;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import com.arizonsoftware.axtonsemotes.commands.BaseCommands;
import com.arizonsoftware.axtonsemotes.commands.EmoteCommand;
import com.arizonsoftware.axtonsemotes.commands.ListEmotes;
import com.arizonsoftware.axtonsemotes.commands.ToggleSharedEmotes;
import com.arizonsoftware.axtonsemotes.lib.listeners.PlayerEventsListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;

/**
 * Utility class responsible for registering all plugin commands, listeners, and logging startup progress.
 */
public class Registry {

   // Startup counters
   public static int COMMANDS_COUNT = 0;
   public static int LISTENERS_COUNT = 0;
   public static int CONFIGS_COUNT = 0;

   private Registry() {
      throw new IllegalStateException("Utility class");
   }

   /**
    * Registers all plugin components: commands, listeners, and logs the progress.
    *
    * @param startTime The time in milliseconds when plugin initialization started (used for logging duration).
    */
   public static void registerAll(long startTime) {
      registerCommands();
      registerListeners();
      logProgress(startTime);
      resetCounter();
   }

   /** Registers all plugin commands */
   private static void registerCommands() {
      registerCommand(AxtonsEmotes.getInstance().getCommand("axtonsemotes"), new BaseCommands());
      registerCommand(AxtonsEmotes.getInstance().getCommand("emotes"), new ListEmotes("shared"));
      registerCommand(AxtonsEmotes.getInstance().getCommand("expressions"), new ListEmotes("expression"));
      registerCommand(AxtonsEmotes.getInstance().getCommand("emote"), new EmoteCommand());
      registerCommand(AxtonsEmotes.getInstance().getCommand("toggleemotes"), new ToggleSharedEmotes());
   }

   /** Registers all plugin event listeners */
   private static void registerListeners() {
      registerListener(new PlayerEventsListener());
   }

   /**
    * Logs plugin startup progress for commands, listeners, and configuration files.
    *
    * @param startTime The time in milliseconds when plugin initialization started.
    */
   private static void logProgress(long startTime) {
      String progressSuffix = CONFIGS_COUNT > 0 && COMMANDS_COUNT > 0 && LISTENERS_COUNT > 0
            ? MessageHandler.get("plugin.startup.progress_suffix.loaded")
            : MessageHandler.get("plugin.startup.progress_suffix.reloaded");

      String[] subjects = { "progress_suffix", "plugin_name", "current_version", "configs_count", "commands_count",
            "listeners_count" };
      String[] replacements = { progressSuffix,
            AxtonsEmotes.getInstance().getDescription().getName(),
            AxtonsEmotes.getInstance().getDescription().getVersion(),
            String.valueOf(CONFIGS_COUNT),
            String.valueOf(COMMANDS_COUNT),
            String.valueOf(LISTENERS_COUNT) };

      if (CONFIGS_COUNT > 0)
         logMessage("plugin.startup.registry.configs_loaded", subjects, replacements);
      if (COMMANDS_COUNT > 0)
         logMessage("plugin.startup.registry.commands_loaded", subjects, replacements);
      if (LISTENERS_COUNT > 0)
         logMessage("plugin.startup.registry.listeners_loaded", subjects, replacements);
   }

   /**
    * Logs a formatted message using MessageHandler and Debugging.
    *
    * @param messageKey   The message key in the language files.
    * @param subjects     The placeholder names in the message.
    * @param replacements The replacement values for the placeholders.
    */
   private static void logMessage(String messageKey, String[] subjects, String[] replacements) {
      String message = MessageHandler.get(messageKey);
      Debugging.raw("message", MessageHandler.format(message, subjects, replacements));
   }

   /**
    * Registers a plugin command with a given executor.
    *
    * @param command  The plugin command to register.
    * @param executor The command executor or tab completer implementing CommandExecutor and/or TabCompleter.
    * @throws IllegalArgumentException If the executor does not implement CommandExecutor or TabCompleter.
    */
   public static void registerCommand(PluginCommand command, Object executor) {
      if (!(executor instanceof TabCompleter) && !(executor instanceof CommandExecutor)) {
         throw new IllegalArgumentException("Executor must implement TabCompleter or CommandExecutor");
      }
      if (command != null) {
         command.setExecutor((CommandExecutor) executor);
         if (executor instanceof TabCompleter)
            command.setTabCompleter((TabCompleter) executor);
         ++COMMANDS_COUNT;
      }
   }

   /**
    * Registers a plugin event listener.
    *
    * @param listener The listener instance to register.
    */
   public static void registerListener(Listener listener) {
      Bukkit.getServer().getPluginManager().registerEvents(listener, AxtonsEmotes.getInstance());
      ++LISTENERS_COUNT;
   }

   /** Resets the counters for commands, listeners, and configuration files. */
   public static void resetCounter() {
      COMMANDS_COUNT = 0;
      LISTENERS_COUNT = 0;
      CONFIGS_COUNT = 0;
   }
}
