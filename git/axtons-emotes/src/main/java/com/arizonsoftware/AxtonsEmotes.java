package com.arizonsoftware;

import com.arizonsoftware.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AxtonsEmotes extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand("hug")).setExecutor(new Hug());
        Objects.requireNonNull(getCommand("kiss")).setExecutor(new Kiss());
        Objects.requireNonNull(getCommand("poke")).setExecutor(new Poke());
        Objects.requireNonNull(getCommand("highfive")).setExecutor(new HighFive());
        Objects.requireNonNull(getCommand("thank")).setExecutor(new Thank());
        Objects.requireNonNull(getCommand("slap")).setExecutor(new Slap());

        // Output plugin startup
        getLogger().info("Axton's Emotes has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
