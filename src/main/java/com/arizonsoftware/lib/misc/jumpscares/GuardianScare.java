package com.arizonsoftware.lib.misc.jumpscares;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.arizonsoftware.Main;

public class GuardianScare {
	@SuppressWarnings("deprecation")
	public static void execute(Player player, String potionEffect, String soundEffect, String particleEffect) {

		// Determine command effects from configuration
		PotionEffectType fxPotionType = isValidPotionEffect(potionEffect) ? PotionEffectType.getByName(potionEffect) : null;
		PotionEffect fxPotion = fxPotionType != null ? new PotionEffect(fxPotionType, 45, 1, false, false, false) : null;
		Sound fxSound = isValidSoundEffect(soundEffect) ? Sound.valueOf(soundEffect) : null;
		Particle fxParticle = isValidParticleEffect(particleEffect) ? Particle.valueOf(particleEffect) : null;

		// Get player location
		final Location playerLocation = player.getLocation();

		// Execute effects on player
		if (fxPotion != null) {
			player.addPotionEffect(fxPotion);
		}
		if (fxSound != null) {
			player.playSound(player.getLocation(), fxSound, 1, 1);
		}
		if (fxParticle != null) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
				player.spawnParticle(fxParticle, playerLocation, 100, 1, 1, 1);
			}, 1);
		}
	}

	/**
	 * Checks if a given potion effect is valid.
	 *
	 * @param potionEffect the potion effect to check
	 * @return true if the potion effect is valid, false
	 *         otherwise
	 */
	private static boolean isValidPotionEffect(String potionEffect) {
		return potionEffect != null && !potionEffect.equalsIgnoreCase("none") && PotionEffectType.getByName(potionEffect) != null;
	}

	/**
	 * Checks if a given sound effect is valid.
	 *
	 * @param soundEffect the sound effect to be checked
	 * @return true if the sound effect is valid, false
	 *         otherwise
	 */
	private static boolean isValidSoundEffect(String soundEffect) {
		return soundEffect != null && !soundEffect.equalsIgnoreCase("none") && Sound.valueOf(soundEffect) != null;
	}

	/**
	 * Checks if a given particle effect is valid.
	 *
	 * @param particleEffect the particle effect to check
	 * @return true if the particle effect is valid, false
	 *         otherwise
	 */
	private static boolean isValidParticleEffect(String particleEffect) {
		return particleEffect != null && !particleEffect.equalsIgnoreCase("none") && Particle.valueOf(particleEffect) != null;
	}
}