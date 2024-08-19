package com.arizonsoftware.commands.handlers;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.utils.Strings;
import com.arizonsoftware.utils.Validation;

import static org.bukkit.Bukkit.getServer;

public class SharedEmotes {

    private static Command thisCommand;
    private String playerResponse = null; // set default
    private String targetResponse = null; // set default
    private Particle particle = null; // set default
    private Sound sound = null; // set default

    /**
     * Creates a new instance of SharedEmotes.
     * 
     * @param command The command object.
     * @return A new instance of SharedEmotes.
     */
    public static SharedEmotes create(Command command) {
        thisCommand = command;
        return new SharedEmotes();
    }

    /**
     * Sets the response messages for the player and target.
     *
     * @param responsePlayer The response message for the player.
     * @param responseTarget The response message for the target.
     */
    public void setResponses(String responsePlayer, String responseTarget) {
        this.playerResponse = responsePlayer;
        this.targetResponse = responseTarget;
    }

    /**
     * Sets the particle effect and sound for the emote.
     *
     * @param particle the particle effect to set
     * @param sound    the sound effect to set
     */
    public void setFX(Particle particle, Sound sound) {
        this.particle = particle;
        this.sound = sound;
    }

    /**
     * Executes the shared emotes command.
     *
     * @param args    the command arguments
     * @param command the command being executed
     * @param sender  the command sender
     */
    public void execute(@NotNull String[] args, @NotNull Command command, @NotNull CommandSender sender) {

        // Validate command execution
        if (!validate(args, thisCommand, sender))
            return;

        // Get targets and player
        Player target = getServer().getPlayer(args[0]);
        assert target != null;
        Player player = (Player) sender;
        assert player != null;

        // Notify sender
        if (playerResponse != null)
            player.sendMessage(Strings
                    .parse(playerResponse).replaceAll("%target%", target.getName()));

        // Notify argument player
        if (targetResponse != null)
            target.sendMessage(
                    Strings.parse(targetResponse).replaceAll("%player%", player.getName()));

        // Emit particles on both players
        if (this.particle != null)
            player.getWorld().spawnParticle(this.particle, target.getLocation(), 3, 0.5,
                    0.5, 0.5, 0);
        if (this.particle != null)
            target.getWorld().spawnParticle(this.particle, target.getLocation(), 3, 0.5,
                    0.5, 0.5, 0);

        // Play sound to both players
        if (this.sound != null)
            player.getWorld().playSound(target.getLocation(), this.sound,
                    1, 1);
        if (this.sound != null)
            target.getWorld().playSound(target.getLocation(), this.sound,
                    1, 1);
    }

    /**
     * Validates the execution of a command by checking various conditions.
     * 
     * @param args    the command arguments
     * @param command the command being executed
     * @param sender  the command sender
     * @return true if the execution is valid, false otherwise
     */
    private static boolean validate(String[] args, Command command, CommandSender sender) {
        // Execution validation
        if (!Validation.checkIsEnabled(command, sender))
            return false;
        if (!Validation.checkIsPlayer(sender))
            return false;
        if (!Validation.checkHasPermission(sender, command))
            return false;
        if (!Validation.checkArguments(sender, args))
            return false;
        if (!Validation.checkSelfExecution(sender, args))
            return false;
        if (!Validation.checkIsOnline(sender, args))
            return false;
        return true;
    }

}