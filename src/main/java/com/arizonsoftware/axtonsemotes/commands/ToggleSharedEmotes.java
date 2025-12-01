package com.arizonsoftware.axtonsemotes.commands;

import com.arizonsoftware.axtonsemotes.utils.Configuration;
import com.arizonsoftware.axtonsemotes.utils.Debugging;
import com.arizonsoftware.axtonsemotes.utils.MessageHandler;
import com.arizonsoftware.axtonsemotes.utils.PlayerCustomisation;
import com.arizonsoftware.axtonsemotes.utils.Validation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleSharedEmotes implements CommandExecutor {

   /**
    * Executes the shared emote toggle command.
    *
    * Checks whether the sender is a player, verifies configuration settings,
    * handles permissions, and toggles the player's shared emote setting.
    * Sends feedback messages to the player and logs the change.
    *
    * @param sender The command sender executing the toggle (must be a player).
    * @param command The command object associated with the toggle.
    * @param label The label of the command used.
    * @param args The arguments passed to the command (not used here).
    * @return false if the command executed normally; true if it was blocked due to configuration or permissions.
    */
   @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      // Check if sender is a player
      if (!Validation.checkIsSenderPlayer(sender))
         return false;

      // Cast sender to Player
      Player player = (Player) sender;

      // Check if self-toggle is allowed in config
      if (!Configuration.getBoolean("config.yml", "allow-self-toggle.enabled")) {
         sender.sendMessage(MessageHandler.parseError("error.command.disabled"));
         return true;
      }

      // Check if permission is required and if the sender lacks it
      if (Configuration.getBoolean("config.yml", "allow-self-toggle.require-permission") &&
            !sender.hasPermission("axtonsemotes.emotes.selftoggle")) {
         sender.sendMessage(MessageHandler.parseError("error.command.permission"));
         return true;
      }

      // Retrieve current setting
      Boolean currentSetting = PlayerCustomisation.retrieveBooleanPlayerSetting("toggleemotes", player);
      Boolean newSetting = (currentSetting != null && !currentSetting) ? Boolean.TRUE : Boolean.FALSE;

      // Update player setting
      PlayerCustomisation.storePlayerSetting("toggleemotes", player, newSetting);

      // Send feedback message
      if (newSetting) {
         player.sendMessage(MessageHandler.parseInfo("command.emote_toggle.enabled"));
      } else {
         player.sendMessage(MessageHandler.parseInfo("command.emote_toggle.disabled"));
      }

      // Log the change
      String logContext = this.getClass().getSimpleName() + "/"
            + Thread.currentThread().getStackTrace()[1].getMethodName();
      Debugging.log(logContext, player.getName() + " set shared emotes to: " + newSetting);

      return false;
   }
}
