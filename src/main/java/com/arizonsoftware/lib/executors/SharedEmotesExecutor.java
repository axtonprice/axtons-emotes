package com.arizonsoftware.lib.executors;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.lib.handlers.SharedEmotesHandler;
import com.arizonsoftware.lib.misc.CommandPlaceholders;
import com.arizonsoftware.utils.Configuration;
import com.arizonsoftware.utils.Debugging;
import com.arizonsoftware.utils.MessageHandler;

public class SharedEmotesExecutor implements CommandExecutor {

        /**
         * Executes the command when it is triggered.
         *
         * @param sender  the command sender
         * @param command the command being executed
         * @param label   the command label
         * @param args    the command arguments
         * @return true if the command was executed successfully,
         *         false otherwise
         */
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

                // Debug logging
                Debugging.log(this.getClass().getSimpleName() + "/onCommand", "Executing command: " + command.getLabel());

                // Create new SharedEmotes handler
                SharedEmotesHandler emote = new SharedEmotesHandler();
                emote.create(command, sender);

                // Fetch messages and effects from configuration
                String playerMessage = Configuration.getString("commands.yml", "commands." + command.getLabel() + ".messages.player");
                String targetMessage = Configuration.getString("commands.yml", "commands." + command.getLabel() + ".messages.target");
                String configParticle = Configuration.getString("commands.yml", "commands." + command.getLabel() + ".effects.particle");
                String configSound = Configuration.getString("commands.yml", "commands." + command.getLabel() + ".effects.sound");

                // Fetch particle and sound from configuration
                Particle fxParticle = null;
                Sound fxSound = null;
                try {
                        if (configParticle != null && !configParticle.isEmpty() && !configParticle.equals("none")) {
                                fxParticle = Particle.valueOf(configParticle);
                        }
                        if (configSound != null && !configSound.isEmpty() && !configSound.equals("none")) {
                                fxSound = Sound.valueOf(configSound);
                        }
                } catch (IllegalArgumentException e) {
                        Debugging.log(this.getClass().getSimpleName() + "/onCommand", "[!] Invalid particle or sound configuration: " + e.getMessage());
                        sender.sendMessage(MessageHandler.get("message_error_invalid_config"));
                        return false;
                }

                // Create an instance of CommandPlaceholders
                CommandPlaceholders placeholders = new CommandPlaceholders(command);

                // Generate the random result once
                String randomResult = CommandPlaceholders.generateRandomResult();

                // Replace placeholders in the messages
                playerMessage = placeholders.replaceString(playerMessage, randomResult);
                targetMessage = placeholders.replaceString(targetMessage, randomResult);

                // Debug logging
                Debugging.log(this.getClass().getSimpleName() + "/onCommand", "Fetched particle: " + fxParticle);
                Debugging.log(this.getClass().getSimpleName() + "/onCommand", "Fetched sound: " + fxSound);
                Debugging.log(this.getClass().getSimpleName() + "/onCommand", "Fetched playerMessage: " + playerMessage);
                Debugging.log(this.getClass().getSimpleName() + "/onCommand", "Fetched targetMessage: " + targetMessage);

                // Set emote data
                emote.setResponses(playerMessage, targetMessage);
                emote.setFX(fxParticle, fxSound);
                emote.execute(args, command);

                return true;
        }
}