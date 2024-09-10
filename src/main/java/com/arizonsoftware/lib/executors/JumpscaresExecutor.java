package com.arizonsoftware.lib.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.lib.handlers.JumpscareHandler;
import com.arizonsoftware.lib.misc.CommandPlaceholders;
import com.arizonsoftware.utils.Configuration;
import com.arizonsoftware.utils.Debugging;

public class JumpscaresExecutor implements CommandExecutor {

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

		// Create new jumpscare handler
		JumpscareHandler jumpscare = new JumpscareHandler();
		jumpscare.create(command, sender);

		// Fetch effects from configuration
		String configPotion = Configuration.getString("commands.yml", "commands." + command.getLabel() + ".effects.potion");
		String configSound = Configuration.getString("commands.yml", "commands." + command.getLabel() + ".effects.sound");
		String configParticle = Configuration.getString("commands.yml", "commands." + command.getLabel() + ".effects.particle");

		// Debug log
		Debugging.log(this.getClass().getSimpleName() + "/onCommand", "Fetched potion: " + configPotion);
		Debugging.log(this.getClass().getSimpleName() + "/onCommand", "Fetched sound: " + configSound);
		Debugging.log(this.getClass().getSimpleName() + "/onCommand", "Fetched particle: " + configParticle);

		// Create an instance of CommandPlaceholders
		CommandPlaceholders placeholders = new CommandPlaceholders(command);

		// Generate the random result once
		String randomResult = CommandPlaceholders.generateRandomResult();

		// Replace placeholders in the messages
		configPotion = placeholders.replaceString(configPotion, randomResult);
		configSound = placeholders.replaceString(configSound, randomResult);
		configParticle = placeholders.replaceString(configParticle, randomResult);

		// Set jumpscare effects
		jumpscare.setEffects(configPotion, configSound, configParticle);
		jumpscare.execute(args, command);

		return true;
	}
}