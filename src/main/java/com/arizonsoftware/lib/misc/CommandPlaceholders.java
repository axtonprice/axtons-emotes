package com.arizonsoftware.lib.misc;

import org.bukkit.command.Command;

import java.util.Random;

public class CommandPlaceholders {
	Command command;
	String commandLabel;
	String[] subjects = null;
	String[] replacements = null;

	/**
	 * Constructs a new CommandPlaceholders object with the
	 * specified Command.
	 *
	 * @param command the Command object to be associated with
	 *                the CommandPlaceholders
	 */
	public CommandPlaceholders(Command command) {
		this.command = command;
	}

	/**
	 * Replaces placeholders in a given message with
	 * corresponding values.
	 * 
	 * @param message      The original message with
	 *                     placeholders.
	 * @param randomResult The value to replace the "result"
	 *                     placeholder with.
	 * @return The message with replaced placeholders.
	 */
	public String replaceString(String message, String randomResult) {
		this.commandLabel = command.getLabel();
		if (commandLabel.equals("dapup") || commandLabel.equals("expression") || commandLabel.equals("jumpscare")) {
			this.subjects = new String[] { "result" };
			this.replacements = new String[] { randomResult };
		}

		if (this.subjects == null || this.replacements == null)
			return message;
		for (int i = 0; i < subjects.length; i++) {
			message = message.replace("%" + subjects[i] + "%", replacements[i]);
		}
		return message;
	}

	public static String generateRandomResult() {
		/**
		 * Represents an array of variations for a specific command.
		 * Each variation represents a different outcome or result.
		 */
		String[] variations = { "&aGood clap!", "&7Near miss..!", "&cMiss!" };
		return variations[new Random().nextInt(variations.length)];
	}
}