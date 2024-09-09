package com.arizonsoftware.lib.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.utils.Debugging;
import com.arizonsoftware.utils.MessageHandler;
import com.arizonsoftware.utils.Validation;

import static org.bukkit.Bukkit.getServer;

public class ExpressionsHandler extends BaseHandler {

    private String playerResponse = null;
    private String globalResponse = null;

    /**
     * Creates an instance of ExpressionsHandler.
     * 
     * @param command the command object
     * @param sender  the command sender
     * @return an instance of ExpressionsHandler
     */
    public ExpressionsHandler create(Command command, CommandSender sender) {
        Debugging.log(this.getClass().getSimpleName() + "/create", "Constructing: " + command.getLabel());
        initialize(command, sender);
        return new ExpressionsHandler();
    }

    /**
     * Sets the responses for the player and global
     * interactions.
     *
     * @param playerResponse The response for the player
     *                       interaction.
     * @param globalResponse The response for the global
     *                       interaction.
     */
    public void setResponses(String playerResponse, String globalResponse) {
        this.playerResponse = playerResponse;
        this.globalResponse = globalResponse;
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param args    the arguments passed to the command
     * @param command the command to be executed
     */
    @Override
    public void execute(@NotNull String[] args, @NotNull Command command) {
        Debugging.log(this.getClass().getSimpleName() + "/execute", "Executing: " + command.getLabel());

        if (!validate(args))
            return;

        Player player = (Player) thisSender;

        if (playerResponse != null) {
            player.sendMessage(MessageHandler.parseColor(playerResponse));
        }

        if (globalResponse != null) {
            for (Player onlinePlayer : getServer().getOnlinePlayers()) {
                if (!onlinePlayer.equals(player)) {
                    onlinePlayer.sendMessage(MessageHandler.parseColor(globalResponse.replace("%player%", player.getName()).replace("%player%", player.getName())));
                }
            }
        }

        emitSound(player);
        emitParticle(player);

        Debugging.log(this.getClass().getSimpleName() + "/execute", "Received sound: " + sound);
        Debugging.log(this.getClass().getSimpleName() + "/execute", "Received particle: " + particle);
        Debugging.log(this.getClass().getSimpleName() + "/execute", "Received playerResponse: " + playerResponse);
        Debugging.log(this.getClass().getSimpleName() + "/execute", "Received globalResponse: " + globalResponse);
    }

    /**
     * Validates the arguments for the command.
     * 
     * @param args the arguments to be validated
     * @return true if the arguments are valid, false otherwise
     */
    protected boolean validate(String[] args) {
        if (!Validation.checkIsEnabled(thisCommand, thisSender)) {
            Debugging.log(this.getClass().getSimpleName() + "/validate", "Validation failed: Command not enabled");
            return false;
        }
        if (!Validation.checkIsSenderPlayer(thisSender)) {
            Debugging.log(this.getClass().getSimpleName() + "/validate", "Validation failed: Sender is not a player");
            return false;
        }
        return true;
    }
}