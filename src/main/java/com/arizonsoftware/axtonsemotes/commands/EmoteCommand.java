package com.arizonsoftware.axtonsemotes.commands;

import com.arizonsoftware.axtonsemotes.lib.handlers.EmoteHandler;
import com.arizonsoftware.axtonsemotes.utils.Configuration;
import com.arizonsoftware.axtonsemotes.utils.Debugging;
import com.arizonsoftware.axtonsemotes.utils.MessageHandler;
import com.arizonsoftware.axtonsemotes.utils.Validation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmoteCommand implements TabExecutor {

   /**
    * Executes the emote command.
    * Performs validation checks including:
    * - Ensuring the sender is a player
    * - Permission checks
    * - Emote existence and configuration validity
    * - Target online and self-execution checks for shared emotes
    *
    * @param sender The command sender (player).
    * @param command The command object being executed.
    * @param label The command label used by the sender.
    * @param args Command arguments, where args[0] is the emote name and args[1] may be a target player.
    * @return true if execution proceeds without halting due to an error.
    */
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
         @NotNull String[] args) {

      // Validate sender is a player and has permission
      if (!Validation.checkIsSenderPlayer(sender))
         return true;

      // Check if sender has permission to use emotes
      if (!sender.hasPermission("axtonsemotes.emotes.use") && !sender.isOp()) {
         Debugging.log(this.getClass().getSimpleName(),
               "Execution failed: Player " + sender.getName()
                     + " does not have permission 'axtonsemotes.emotes.use' to use emotes.");
         sender.sendMessage(MessageHandler.parseError("error.permission"));
         return true;
      }

      // Validate presence of arguments
      if (args.length == 0) {
         Debugging.log(this.getClass().getSimpleName(),
               "Execution failed: Player '" + sender.getName() + "' did not specify an emote to execute.");
         sender.sendMessage(MessageHandler.parseError("error.command.syntax.emote_shared"));
         return true;
      }

      // Fetch and validate emote name
      String emoteName = args[0].toLowerCase();

      // Validate emote existence and configuration
      if (!this.getAvailableEmotes(sender).contains(emoteName)) {
         Debugging.log(this.getClass().getSimpleName(),
               "Execution failed: Unknown emote '" + emoteName + "' by " + sender.getName());
         sender.sendMessage(MessageHandler.parseError("error.emote.not_found"));
         return true;
      }

      // Validate emote configuration
      if (!Validation.validateEmoteConfig(emoteName)) {
         Debugging.log(
               this.getClass().getSimpleName() + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
               "Execution failed: Emote '" + emoteName + "' has invalid or absent configuration keys.");
         sender.sendMessage(MessageHandler.parseError("error.emote.misconfigured"));
         return true;
      }

      // Check if emote is enabled
      if (!this.isEmoteEnabled(emoteName)) {
         Debugging.log(this.getClass().getSimpleName(),
               "Execution failed: Invalid emote '" + emoteName + "' by " + sender.getName());
         sender.sendMessage(MessageHandler.parseError("error.emote.not_found"));
         return true;
      }

      // Fetch emote type and perform shared emote checks
      String emoteType = Configuration.getString("emotes.yml", "commands." + emoteName + ".type");

      // Shared emote requires target checks
      if (emoteType.equalsIgnoreCase("shared")) {

         // Validate presence of target argument
         if (args.length < 2) {
            Debugging.log(this.getClass().getSimpleName(),
                  "Execution failed: Missing target for shared emote '" + emoteName + "' by " + sender.getName());
            sender.sendMessage(MessageHandler.parseError("error.command.syntax.emote_shared"));
            return true;
         }

         // Validate target online
         if (!Validation.checkIsTargetOnline(sender, new String[] { args[1] }))
            return true;

         // Validate self-execution
         if (!Validation.checkSelfExecution(sender, new String[] { args[1] }))
            return true;
      }

      // Execute emote
      EmoteCommandWrapper emoteCommand = new EmoteCommandWrapper(emoteName);
      String[] emoteArgs = new String[args.length - 1];
      System.arraycopy(args, 1, emoteArgs, 0, emoteArgs.length);

      // Build and execute the emote
      return EmoteHandler.buildEmote(sender, emoteCommand, emoteName, emoteArgs);

   }

   /**
    * Provides tab completion for emotes.
    *
    * @param sender The command sender.
    * @param command The command object being executed.
    * @param label The command label.
    * @param args The current arguments; used to filter completions.
    * @return A list of possible completions, or null for player name completion in shared emotes.
    */
   @Nullable
   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
         @NotNull String[] args) {
      if (args.length == 1) {
         // Provide tab completion for emote names
         String emoteName = args[0].toLowerCase();
         return this.getAvailableEmotes(sender).stream().filter(name -> name.toLowerCase().startsWith(emoteName))
               .toList();
      } else if (args.length == 2) {
         // Provide tab completion for target player names if emote is shared
         String emoteName = args[0].toLowerCase();
         String type = Configuration.getString("emotes.yml", "commands." + emoteName + ".type");

         // If emote is shared, return null to suggest player names
         if ("shared".equalsIgnoreCase(type)) {
            return null; // Suggest player names
         }
      }
      return new ArrayList<>();
   }

   /**
    * Checks whether a given emote is enabled in configuration.
    *
    * @param emoteName The emote name to check.
    * @return true if enabled, false otherwise.
    */
   private boolean isEmoteEnabled(String emoteName) {
      Boolean enabled = Configuration.getBoolean("emotes.yml", "commands." + emoteName + ".enabled");
      return enabled != null && enabled;
   }

   /**
    * Retrieves the list of emotes available to a sender based on permissions and enabled state.
    *
    * @param sender The command sender to check permissions for.
    * @return A list of available emote names.
    */
   private List<String> getAvailableEmotes(CommandSender sender) {
      // Compile list of available emotes
      List<String> emotes = new ArrayList<>();
      Set<String> emoteNames = Configuration.getConfigSectionKeys("emotes.yml", "commands");

      // Iterate through emote names and filter by enabled state and permission
      for (String name : emoteNames) {
         // Check if emote is enabled and sender has permission
         if (this.isEmoteEnabled(name) && sender.hasPermission("axtonsemotes.emotes.use")) {
            emotes.add(name);
         }
      }
      return emotes;
   }

   /**
    * A dummy Command wrapper used to satisfy UnifiedEmoteExecutor's expected Command parameter.
    */
   private static class EmoteCommandWrapper extends Command {
      // Constructor
      public EmoteCommandWrapper(String name) {
         super(name);
      }

      // Execute method overridden to satisfy Command abstract class requirements
      @Override
      public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
         return false;
      }
   }
}
