package com.arizonsoftware.lib.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.utils.Debugging;
import com.arizonsoftware.utils.MessageHandler;
import com.arizonsoftware.utils.Validation;

import static org.bukkit.Bukkit.getServer;

public class SharedEmotesHandler extends BaseHandler {

    private String playerResponse = null;
    private String targetResponse = null;

    /**
     * Creates an instance of SharedEmotesHandler.
     * 
     * @param command the command object
     * @param sender  the command sender
     * @return an instance of SharedEmotesHandler
     */
    public SharedEmotesHandler create(Command command, CommandSender sender) {
        Debugging.log(this.getClass().getSimpleName() + "/create", "Constructing: " + command.getLabel());
        initialize(command, sender);
        return new SharedEmotesHandler();
    }

    /**
     * Sets the responses for the player and target.
     * 
     * @param playerResponse The response for the player.
     * @param targetResponse The response for the target.
     */
    public void setResponses(String playerResponse, String targetResponse) {
        this.playerResponse = playerResponse;
        this.targetResponse = targetResponse;
    }

    /**
     * Executes the shared emotes command.
     *
     * @param args    the command arguments
     * @param command the command object
     */
    @Override
    public void execute(@NotNull String[] args, @NotNull Command command) {
        Debugging.log(this.getClass().getSimpleName() + "/execute", "Executing: " + command.getLabel());

        if (!validate(args))
            return;

        Player player = (Player) thisSender;
        Player target = getServer().getPlayer(args[0]);
        if (target == null) {
            thisSender.sendMessage(MessageHandler.get("message_error_player_offline"));
            return;
        }

        if (playerResponse != null) {
            player.sendMessage(MessageHandler.parseColor(playerResponse.replace("%target%", target.getName())));
        }

        if (targetResponse != null) {
            target.sendMessage(MessageHandler.parseColor(targetResponse.replace("%player%", player.getName())));
        }

        emitSound(player);
        emitParticle(player);

        Debugging.log(this.getClass().getSimpleName() + "/execute", "Received sound: " + sound);
        Debugging.log(this.getClass().getSimpleName() + "/execute", "Received particle: " + particle);
        Debugging.log(this.getClass().getSimpleName() + "/execute", "Received playerResponse: " + playerResponse);
        Debugging.log(this.getClass().getSimpleName() + "/execute", "Received targetResponse: " + targetResponse);
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
        if (!Validation.checkArguments(thisSender, args)) {
            Debugging.log(this.getClass().getSimpleName() + "/validate", "Validation failed: Invalid arguments");
            return false;
        }
        if (!Validation.checkSelfExecution(thisSender, args)) {
            Debugging.log(this.getClass().getSimpleName() + "/validate", "Validation failed: Self execution not allowed");
            return false;
        }
        if (!Validation.checkIsTargetOnline(thisSender, args)) {
            Debugging.log(this.getClass().getSimpleName() + "/validate", "Validation failed: Target player is not online");
            return false;
        }
        return true;
    }
}