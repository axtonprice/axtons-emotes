package com.arizonsoftware;

import org.bukkit.plugin.java.JavaPlugin;

import com.arizonsoftware.utils.Configuration;
import com.arizonsoftware.utils.Registry;
import com.arizonsoftware.utils.Versioning;

/**
 * Represents the main class of the plugin. This class
 * extends the JavaPlugin class and is responsible for
 * handling the plugin's lifecycle events.
 */
public final class Main extends JavaPlugin {

    // Timings variables
    public static long startTime;

    /**
     * Called when the plugin is loaded. Checks if the plugin
     * directory exists and creates it if it doesn't. Also saves
     * the default configuration.
     */
    @Override
    public void onLoad() {
        // Setup and load configurations
        Configuration.setupConfig();
    }

    /**
     * Called when the plugin is enabled. This method is
     * responsible for registering commands and listeners, as
     * well as resetting the counter in the registry.
     */
    @Override
    public void onEnable() {

        // Set timings variables
        startTime = System.currentTimeMillis();

        // Check plugin version
        Versioning.checkVersion();

        // Register commands and listeners
        Registry.registerAll(startTime);
    }

    /**
     * Returns the instance of the Main class.
     *
     * @return The instance of the Main class.
     */
    public static Main getInstance() {
        return getPlugin(Main.class);
    }

}
