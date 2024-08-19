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

public class Expressions {

    private static Command thisCommand;
    private String playerResponse = "&eYou wave to everyone!"; // set default
    private String globalResponse = "&e%player% waves to everyone!"; // set default
    private Particle particle = null; // set default
    private Sound sound = null; // set default

    /**
     * Creates a new instance of SharedEmotes.
     * 
     * @param command The command object.
     * @return A new instance of Expressions.
     */
    public static Expressions create(Command command) {
        thisCommand = command;
        return new Expressions();
    }

    /**
     * Sets the responses for the player and global expressions.
     *
     * @param playerResponse The response for the player expression.
     * @param globalResponse The response for the global expression.
     */
    public void setResponses(String playerResponse, String globalResponse) {
        this.playerResponse = playerResponse;
        this.globalResponse = globalResponse;
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
     * Executes the command handler for expressions.
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
        Player player = (Player) sender;
        assert player != null;

        // Notify sender
        player.sendMessage(Strings
                .parse(playerResponse).replaceAll("%player%", player.getName()));

        // Notify all players online
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            if (!onlinePlayer.equals(player))
                onlinePlayer.sendMessage(Strings.parse(globalResponse).replaceAll("%player%", player.getName()));
        }

        // Emit particles on executing player
        if (this.particle != null)
            player.getWorld().spawnParticle(this.particle, player.getLocation(), 3, 0.5, 0.5, 0.5, 0);

        // Play sound to executing player
        if (this.sound != null)
            player.getWorld().playSound(player.getLocation(), this.sound, 1, 1);

    }

    /**
     * Validates the execution of a command.
     *
     * @param args    the command arguments
     * @param command the command to be executed
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
        return true;
    }

}