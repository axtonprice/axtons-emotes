package com.arizonsoftware.lib.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.lib.misc.jumpscares.*;
import com.arizonsoftware.utils.Debugging;
import com.arizonsoftware.utils.Validation;

public class JumpscareHandler extends BaseHandler {

	private String potionEffect;
	private String soundEffect;
	private String particleEffect;

	/**
	 * Creates a new JumpscareHandler object.
	 * 
	 * @param command The command object.
	 * @param sender  The command sender.
	 * @return The newly created JumpscareHandler object.
	 */
	public JumpscareHandler create(Command command, CommandSender sender) {
		Debugging.log(this.getClass().getSimpleName() + "/create", "Constructing: " + command.getLabel());
		initialize(command, sender);
		return new JumpscareHandler();
	}

	/**
	 * Sets the effects for the jumpscare.
	 *
	 * @param potionEffect   the potion effect to apply
	 * @param soundEffect    the sound effect to play
	 * @param particleEffect the particle effect to display
	 */
	public void setEffects(String potionEffect, String soundEffect, String particleEffect) {
		this.potionEffect = potionEffect;
		this.soundEffect = soundEffect;
		this.particleEffect = particleEffect;
	}

	/**
	 * Executes the command with the given arguments.
	 *
	 * @param args    the arguments passed to the command
	 * @param command the command object
	 */
	@Override
	public void execute(@NotNull String[] args, @NotNull Command command) {
		// Debug log
		Debugging.log(this.getClass().getSimpleName() + "/execute", "Executing: " + command.getLabel());

		// Validate execution
		if (!validate(args))
			return;

		// Get player from sender
		Player player = (Player) thisSender;

		// Check for null values and log errors
		if (potionEffect == null || soundEffect == null || particleEffect == null) {
			Debugging.log(this.getClass().getSimpleName() + "/execute", "Error: Missing effect configuration");
			player.sendMessage("Error: Missing effect configuration. Please check the commands.yml file.");
			return;
		}

		// Determine and execute the command
		if (command.getLabel().equals("guardianscare"))
			GuardianScare.execute(player, potionEffect, soundEffect, particleEffect);
		if (command.getLabel().equals("bogscare"))
			BogScare.execute(player, potionEffect, soundEffect, particleEffect);

		// Debug log
		Debugging.log(this.getClass().getSimpleName() + "/execute", "Received potion: " + potionEffect);
		Debugging.log(this.getClass().getSimpleName() + "/execute", "Received sound: " + soundEffect);
		Debugging.log(this.getClass().getSimpleName() + "/execute", "Received particle: " + particleEffect);
	}

	/**
	 * Validates the arguments for the JumpscareHandler command.
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