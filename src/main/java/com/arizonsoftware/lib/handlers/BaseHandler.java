package com.arizonsoftware.lib.handlers;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.utils.Debugging;

import org.bukkit.Bukkit;

public abstract class BaseHandler {

    protected static Command thisCommand;
    protected static CommandSender thisSender = null;
    protected Particle particle = null;
    protected Sound sound = null;

    /**
     * Initializes the BaseHandler with the given command and
     * command sender.
     *
     * @param command the command to be initialized
     * @param sender  the command sender to be initialized
     */
    public static void initialize(Command command, CommandSender sender) {
        thisCommand = command;
        thisSender = sender;
    }

    /**
     * Sets the particle effect and sound for the handler.
     * 
     * @param particle The particle effect to set.
     * @param sound    The sound to set.
     * @return true if the particle effect and sound were
     *         successfully set, false otherwise.
     */
    public boolean setFX(Particle particle, Sound sound) {
        if (particle == Particle.DUST) {
            Bukkit.getLogger().severe("[!] Unsupported particle effect: " + particle + " in emote command: " + thisCommand.getLabel());
            this.particle = null;
            this.sound = sound;
            return false;
        }

        this.particle = particle;
        this.sound = sound;
        return true;
    }

    /**
     * Emits a particle effect at the player's location.
     *
     * @param player the player to emit the particle effect to
     */
    protected void emitParticle(Player player) {
        if (particle == null)
            return;
        try {
            player.getWorld().spawnParticle(particle, player.getLocation(), 3, 0.5, 0.5, 0.5, 0);
        } catch (IllegalArgumentException e) {
            Debugging.log(this.getClass().getSimpleName() + "/emitParticle", "Invalid or unsupported particle ID: " + e.getMessage());
        }
    }

    /**
     * Emits a sound to the specified player.
     *
     * @param player the player to emit the sound to
     */
    protected void emitSound(Player player) {
        if (sound == null)
            return;
        try {
            player.getWorld().playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            Debugging.log(this.getClass().getSimpleName() + "/emitSound", "Invalid or unsupported sound ID: " + e.getMessage());
        }
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param args    the arguments for the command
     * @param command the command to be executed
     */
    public abstract void execute(@NotNull String[] args, @NotNull Command command);
}