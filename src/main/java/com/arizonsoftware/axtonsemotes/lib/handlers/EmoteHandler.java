package com.arizonsoftware.axtonsemotes.lib.handlers;

import com.arizonsoftware.axtonsemotes.utils.Configuration;
import com.arizonsoftware.axtonsemotes.utils.Debugging;
import com.arizonsoftware.axtonsemotes.utils.MessageHandler;
import com.arizonsoftware.axtonsemotes.utils.PlayerCustomisation;
import com.arizonsoftware.axtonsemotes.utils.Validation;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EmoteHandler extends FXHandler {
   private String emoteType = "shared";
   private String playerResponse = null;
   private String targetResponse = null;

   /**
    * Builds and executes an emote based on the given command and sender.
    * 
    * This method fetches the emote configuration, validates it, retrieves messages and effects,
    * and executes the emote through UnifiedEmoteHandler.
    *
    * @param sender The command sender (player or console) executing the emote.
    * @param command The command object representing the emote command.
    * @param label The label of the command used.
    * @param args The arguments provided with the command.
    * @return true if the emote was successfully executed.
    */
   public static boolean buildEmote(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
         @NotNull String[] args) {

      // Construct emote
      EmoteHandler emote = new EmoteHandler();
      emote.create(command, sender);

      // Fetch and log emote type
      String emoteType = Configuration.getString("emotes.yml", "commands." + command.getLabel() + ".type");
      String logContext = EmoteHandler.class.getSimpleName() + "/"
            + Thread.currentThread().getStackTrace()[1].getMethodName();

      // Debug logging
      Debugging.log(logContext, "Validating emote: '" + command.getLabel() + "' of type '" + emoteType + "'");
      Debugging.log(logContext,
            "Executing emote: '" + command.getLabel() + "' of type '" + emoteType + "' by "
                  + sender.getName());

      // Fetch messages and effects from configuration
      String playerMessage = Configuration.getString("emotes.yml",
            "commands." + command.getLabel() + ".messages.player");
      String targetMessage = Configuration.getString("emotes.yml",
            "commands." + command.getLabel() + ".messages.target");
      String configParticle = Configuration.getString("emotes.yml",
            "commands." + command.getLabel() + ".effects.particle");
      String configSound = Configuration.getString("emotes.yml",
            "commands." + command.getLabel() + ".effects.sound");

      // Parse effects with defaults
      Particle fxParticle = FXHandler.convertParticle(configParticle, command.getLabel());
      Sound fxSound = FXHandler.convertSound(configSound, command.getLabel());

      // Debug logging
      Debugging.log(logContext, "Fetched FX data :: particle: " + fxParticle + ", sound: " + fxSound);
      Debugging.log(logContext,
            "Fetched message data :: playerMessage: '" + playerMessage + "', secondaryMessage: '"
                  + targetMessage + "'");

      // Add emote properties and execute
      emote.setEmoteType(emoteType);
      emote.setResponses(playerMessage, targetMessage);
      emote.setFX(fxParticle, fxSound);
      emote.execute(args, command);

      return true;
   }

   /**
    * Initializes the handler with a command and sender.
    *
    * @param command The command being executed.
    * @param sender  The player or console executing the command.
    * @return A new instance of UnifiedEmoteHandler.
    */
   public EmoteHandler create(Command command, CommandSender sender) {
      Debugging.log(this.getClass().getSimpleName() + "/create", "Constructing emote: " + command.getLabel());
      initialize(command, sender);
      return new EmoteHandler();
   }

   /**
    * Executes a shared emote targeting another player.
    *
    * @param args   Arguments, where args[0] should be the target player's name.
    * @param player The player executing the emote.
    */
   private void performSharedEmote(String[] args, Player player) {
      if (!validateSharedExecution(args))
         return;

      Player target = Bukkit.getServer().getPlayer(args[0]);
      if (target == null) {
         player.sendMessage(MessageHandler.parseError("error.player.offline"));
         return;
      }

      // Check if the sender has disabled emotes
      Boolean hasSenderToggled = PlayerCustomisation.retrieveBooleanPlayerSetting("toggleemotes", player);
      if (hasSenderToggled != null && !hasSenderToggled) {
         player.sendMessage(MessageHandler.parseErrorReplace("command.emote_toggle.sender_toggled",
               new String[] { "%target%" }, new String[] { target.getName() }));
         return;
      }

      // Check if the target has disabled emotes
      Boolean hasTargetToggledEmotes = PlayerCustomisation.retrieveBooleanPlayerSetting("toggleemotes", target);
      if (hasTargetToggledEmotes != null && !hasTargetToggledEmotes) {
         player.sendMessage(MessageHandler.parseErrorReplace("command.emote_toggle.target_toggled",
               new String[] { "%target%" }, new String[] { target.getName() }));
         return;
      }

      // Send messages to player
      if (!playerResponse.equals("none")) {
         player.sendMessage(MessageHandler.parseColor(playerResponse.replace("%target%", target.getName())));
      }

      // Send message to target if different from player
      if (!player.equals(target)) {
         if (!targetResponse.equals("none")) {
            target.sendMessage(MessageHandler.parseColor(targetResponse.replace("%player%", player.getName())));
         }
      }

      // Emit sound to player and target
      emitSound(player);
      if (!player.equals(target)) // Avoid double emission if same
         emitSound(target);

      // Emit particles to player and target
      emitParticle(player);
      if (!player.equals(target)) // Avoid double emission if same
         emitParticle(target);
   }

   /**
    * Executes an expression emote visible to all players.
    *
    * @param player The player executing the expression.
    */
   private void performExpressionEmote(Player player) {

      // Validate execution
      if (!validateExpressExecution())
         return;

      // Send message to executing player
      if (!"none".equals(playerResponse)) {
         player.sendMessage(MessageHandler.parseColor(playerResponse));
      }

      // Broadcast to all other players
      if (!"none".equals(targetResponse)) {
         for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
               onlinePlayer
                     .sendMessage(MessageHandler.parseColor(targetResponse.replace("%player%", player.getName())));
            }
         }
      }

      // Emit sound and particles to executing player
      emitSound(player);
      emitParticle(player);
   }

   /**
    * Executes the emote based on type.
    *
    * @param args    The command arguments.
    * @param command The command being executed.
    */
   public void execute(@NotNull String[] args, @NotNull Command command) {

      // Cast sender to player
      Player player = (Player) thisSender;

      // Execute based on emote type
      switch (this.emoteType.toLowerCase()) {
         case "shared" -> performSharedEmote(args, player);
         case "expression" -> performExpressionEmote(player);
         default -> performSharedEmote(args, player);
      }

      // Log execution
      Debugging.log(this.getClass().getSimpleName() + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
            "Executed emote '" + command.getLabel() + "' of type: '" + this.emoteType + "' by " + player.getName());
   }

   /**
    * Sets the type of emote (shared, expression).
    *
    * @param emoteType The emote type to set.
    */
   public void setEmoteType(String emoteType) {
      this.emoteType = emoteType != null ? emoteType : "shared";
   }

   /**
    * Sets the player and target messages for the emote.
    *
    * @param playerResponse Message for the executing player.
    * @param targetResponse Message for the target player(s).
    */
   public void setResponses(String playerResponse, String targetResponse) {
      this.playerResponse = playerResponse;
      this.targetResponse = targetResponse;
   }

   /**
    * Validates a shared emote before execution.
    *
    * @param args The command arguments.
    * @return True if execution is valid.
    */
   private boolean validateSharedExecution(String[] args) {
      return Validation.checkIsEnabled(thisCommand, thisSender)
            && Validation.checkIsSenderPlayer(thisSender)
            && Validation.checkArguments(thisSender, args)
            && Validation.checkSelfExecution(thisSender, args)
            && Validation.checkIsTargetOnline(thisSender, args);
   }

   /**
    * Validates an expression emote before execution.
    *
    * @return True if execution is valid.
    */
   private boolean validateExpressExecution() {
      return Validation.checkIsEnabled(thisCommand, thisSender)
            && Validation.checkIsSenderPlayer(thisSender);
   }
}
