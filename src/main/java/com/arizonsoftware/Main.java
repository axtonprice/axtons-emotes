package com.arizonsoftware;

import com.arizonsoftware.commands.expressions.*;
import com.arizonsoftware.commands.misc.*;
import com.arizonsoftware.commands.shared.*;
import com.arizonsoftware.utils.Registry;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    /**
     * Called when the plugin is loaded.
     * Checks if the plugin directory exists and creates it if it doesn't.
     * Also saves the default configuration.
     */
    @Override
    public void onLoad() {
        // Check if plugin directory exists
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir(); // Create directory
            saveDefaultConfig(); // Save default configuration
        }
    }

    /**
     * Called when the plugin is enabled.
     * This method is responsible for registering commands and listeners, as well as
     * resetting the counter in the registry.
     */
    @Override
    public void onEnable() {
        // Register commands and listeners
        RegisterAll();
        Registry.resetCounter();
    }

    /**
     * Registers all the plugin commands and initializes the necessary emotes and
     * expressions.
     * This method should be called during plugin startup to ensure all commands are
     * properly registered.
     * If the commands have already been registered, this method will skip the
     * registration process.
     * 
     * @return void
     */
    public static void RegisterAll() {
        // Timings variables
        long startTime = System.currentTimeMillis();

        // Output startup message
        Bukkit.getLogger().info("---------------------------------");
        Bukkit.getLogger().info("Registering plugin commands..");

        // check if commands have already been registered before registering them again
        if (Registry.commandCount > 0) {
            Bukkit.getLogger().info("Commands already registered! Skipping..");
            return;
        }

        // Register main command
        Registry.registerCommand(getInstance().getCommand("axtonsemotes"), new AxtonsEmotes());

        // Register shared emotes
        Registry.registerCommand(getInstance().getCommand("hug"), new Hug());
        Registry.registerCommand(getInstance().getCommand("kiss"), new Kiss());
        Registry.registerCommand(getInstance().getCommand("poke"), new Poke());
        Registry.registerCommand(getInstance().getCommand("highfive"), new HighFive());
        Registry.registerCommand(getInstance().getCommand("thank"), new Thank());
        Registry.registerCommand(getInstance().getCommand("slap"), new Slap());
        Registry.registerCommand(getInstance().getCommand("dapup"), new DapUp());
        Registry.registerCommand(getInstance().getCommand("stomp"), new Stomp());
        Registry.registerCommand(getInstance().getCommand("stare"), new Stare());
        Registry.registerCommand(getInstance().getCommand("stab"), new Stab());

        // Register expressions
        Registry.registerCommand(getInstance().getCommand("applaud"), new Applaud());
        Registry.registerCommand(getInstance().getCommand("cry"), new Cry());
        Registry.registerCommand(getInstance().getCommand("wave"), new Wave());

        // Register misc commands
        Registry.registerCommand(getInstance().getCommand("bog"), new BogScare());
        Registry.registerCommand(getInstance().getCommand("emotes"), new ListEmotes());
        Registry.registerCommand(getInstance().getCommand("expressions"), new ListExpressions());

        // Timings variables
        long endTime = System.currentTimeMillis();

        // Output finished startup message
        Bukkit.getLogger().info(
                "Successfully registered " + Registry.commandCount + " commands! ("
                        + String.valueOf(endTime - startTime) + "ms)");
        Bukkit.getLogger().info("---------------------------------");
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
