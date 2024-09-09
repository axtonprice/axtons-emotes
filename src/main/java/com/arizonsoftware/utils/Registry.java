package com.arizonsoftware.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;

import com.arizonsoftware.Main;
import com.arizonsoftware.commands.*;
import com.arizonsoftware.lib.listeners.*;
import com.arizonsoftware.lib.executors.*;

public class Registry {

    public static int commandCount = 0;
    public static int listenerCount = 0;
    public static int configsCount = 0;

    /**
     * Registers all the necessary components for the
     * application. This method is responsible for registering
     * commands and listeners, logging the startup message,
     * logging the progress, and resetting the counter.
     *
     * @param startTime the start time of the registration
     *                  process
     */
    public static void registerAll(long startTime) {
        logStartupMessage();

        registerCommands();
        registerListeners();

        logProgress(startTime);
        resetCounter();
    }

    /**
     * Logs the startup message of the plugin.
     */
    private static void logStartupMessage() {
        Bukkit.getLogger().info("---------------------------------");
        Bukkit.getLogger().info("[-] " + MessageHandler.get("message_context_plugin_startup_progress_suffix.starting") + " " + Main.getInstance().getPluginMeta().getName() + " v" + Main.getInstance().getPluginMeta().getVersion());
    }

    /**
     * Registers the commands for the AxtonsEmotes plugin. This
     * method registers various commands and their corresponding
     * executors. The commands include "axtonsemotes", "emotes",
     * "expressions", "jumpscares", as well as a list of shared
     * emotes, expressions, and jumpscares.
     */
    private static void registerCommands() {
        registerCommand(Main.getInstance().getCommand("axtonsemotes"), new AxtonsEmotes());
        registerCommand(Main.getInstance().getCommand("emotes"), new ListEmotes());
        registerCommand(Main.getInstance().getCommand("expressions"), new ListExpressions());
        registerCommand(Main.getInstance().getCommand("jumpscares"), new ListJumpscares());

        String[] emotes = { "hug", "kiss", "poke", "highfive", "thank", "slap", "dapup", "stomp", "stare", "stab", "applaud" };
        for (String emote : emotes) {
            registerCommand(Main.getInstance().getCommand(emote), new SharedEmotesExecutor());
        }

        String[] expressions = { "cry", "wave" };
        for (String expression : expressions) {
            registerCommand(Main.getInstance().getCommand(expression), new ExpressionsExecutor());
        }

        String[] jumpscares = { "bogscare", "guardianscare" };
        for (String jumpscare : jumpscares) {
            registerCommand(Main.getInstance().getCommand(jumpscare), new JumpscaresExecutor());
        }
    }

    /**
     * Registers the listeners for the application.
     */
    private static void registerListeners() {
        registerListener(new PlayerEventsListener());
    }

    /**
     * Logs the progress of the plugin startup.
     *
     * @param startTime The start time of the plugin startup.
     */
    private static void logProgress(long startTime) {
        String progressSuffix = (configsCount <= 0 || commandCount <= 0 || listenerCount <= 0) ? MessageHandler.get("message_context_plugin_startup_progress_suffix.reloaded") : MessageHandler.get("message_context_plugin_startup_progress_suffix.loaded");

        String[] subjects = { "progress_suffix", "plugin_name", "current_version", "configs_count", "commands_count", "listeners_count" };
        String[] replacements = { progressSuffix, Main.getInstance().getPluginMeta().getName(), Main.getInstance().getPluginMeta().getVersion(), String.valueOf(configsCount), String.valueOf(commandCount), String.valueOf(listenerCount) };

        if (configsCount > 0)
            logMessage("message_context_plugin_startup_configs_count", subjects, replacements);
        if (commandCount > 0)
            logMessage("message_context_plugin_startup_commands_count", subjects, replacements);
        if (listenerCount > 0)
            logMessage("message_context_plugin_startup_listeners_count", subjects, replacements);

        Bukkit.getLogger().info("[+] " + MessageHandler.format(MessageHandler.get("message_context_plugin_startup_footer"), subjects, replacements, true) + " " + (System.currentTimeMillis() - startTime) + "ms!");
        Bukkit.getLogger().info("---------------------------------");
    }

    /**
     * Logs a message with the specified message key, subjects,
     * and replacements.
     * 
     * @param messageKey   the key of the message to log
     * @param subjects     an array of subjects to be replaced
     *                     in the message
     * @param replacements an array of replacements for the
     *                     subjects in the message
     */
    private static void logMessage(String messageKey, String[] subjects, String[] replacements) {
        String message = MessageHandler.get(messageKey);
        Bukkit.getLogger().info(MessageHandler.format(message, subjects, replacements, true));
    }

    /**
     * Registers a command with the specified executor.
     *
     * @param command  the PluginCommand to register
     * @param executor the executor object that implements
     *                 TabCompleter or CommandExecutor
     * @throws IllegalArgumentException if the executor does not
     *                                  implement TabCompleter
     *                                  or CommandExecutor
     */
    public static void registerCommand(PluginCommand command, Object executor) {
        if (!(executor instanceof TabCompleter) && !(executor instanceof CommandExecutor)) {
            throw new IllegalArgumentException("Executor must implement TabCompleter or CommandExecutor");
        }

        if (command != null) {
            command.setExecutor((CommandExecutor) executor);
            if (executor instanceof TabCompleter) {
                command.setTabCompleter((TabCompleter) executor);
            }
            commandCount++;
        }
    }

    /**
     * Registers a listener to receive events.
     *
     * @param listener the listener to be registered
     */
    public static void registerListener(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, Main.getInstance());
        listenerCount++;
    }

    /**
     * Resets the counter for command, listener, and configs
     * counts.
     */
    public static void resetCounter() {
        commandCount = 0;
        listenerCount = 0;
        configsCount = 0;
    }
}