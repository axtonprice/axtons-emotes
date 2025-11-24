package com.arizonsoftware.axtonsemotes.lib.handlers;

import com.arizonsoftware.axtonsemotes.utils.Configuration;
import com.arizonsoftware.axtonsemotes.utils.Debugging;
import com.arizonsoftware.axtonsemotes.utils.EffectUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnifiedEmoteBuilder {

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
      public boolean buildEmote(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                  @NotNull String[] args) {

            // Construct emote
            UnifiedEmoteExecutor emote = new UnifiedEmoteExecutor();
            emote.create(command, sender);

            // Fetch and log emote type
            String emoteType = Configuration.getString("emotes.yml", "commands." + command.getLabel() + ".type");
            String logContext = this.getClass().getSimpleName() + "/"
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
            Particle fxParticle = EffectUtils.parseParticleWithDefault(configParticle, command.getLabel());
            Sound fxSound = EffectUtils.parseSoundWithDefault(configSound, command.getLabel());

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
}
