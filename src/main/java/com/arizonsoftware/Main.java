package com.arizonsoftware;

import com.arizonsoftware.commands.expressions.*;
import com.arizonsoftware.commands.shared.*;
import com.arizonsoftware.commands.misc.*;
import com.arizonsoftware.utils.Registry;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        // Save default configuration
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {

        // Output startup message
        getLogger().info("Registering plugin commands..");

        // Register main command
        Registry.registerCommand(getCommand("axtonsemotes"), new AxtonsEmotes());

        // Register shared emotes
        Registry.registerCommand(getCommand("hug"), new Hug());
        Registry.registerCommand(getCommand("kiss"), new Kiss());
        Registry.registerCommand(getCommand("poke"), new Poke());
        Registry.registerCommand(getCommand("highfive"), new HighFive());
        Registry.registerCommand(getCommand("thank"), new Thank());
        Registry.registerCommand(getCommand("slap"), new Slap());
        Registry.registerCommand(getCommand("dap"), new DapUp());
        Registry.registerCommand(getCommand("stomp"), new Stomp());
        Registry.registerCommand(getCommand("stare"), new Stare());
        Registry.registerCommand(getCommand("stab"), new Stab());

        // Register expressions
        Registry.registerCommand(getCommand("applaud"), new Applaud());
        Registry.registerCommand(getCommand("cry"), new Cry());
        Registry.registerCommand(getCommand("sulk"), new Sulk());

        // Register misc commands
        Registry.registerCommand(getCommand("emotes"), new ListEmotes());
        Registry.registerCommand(getCommand("expressions"), new ListExpressions());

        // Output finished startup message
        getLogger().info("Successfully registered " + Registry.commandCount + " commands!");
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    @NotNull
    public Path getConfigDirectory() {
        return getDataFolder().toPath();
    }
}
