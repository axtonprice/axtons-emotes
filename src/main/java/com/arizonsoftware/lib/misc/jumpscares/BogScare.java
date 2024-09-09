package com.arizonsoftware.lib.misc.jumpscares;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.arizonsoftware.Main;

public class BogScare {

	/**
	 * Executes the bog scare effect on the specified player.
	 *
	 * @param player         The player to apply the effect to.
	 * @param potionEffect   The potion effect to apply to the
	 *                       player.
	 * @param soundEffect    The sound effect to play for the
	 *                       player.
	 * @param particleEffect The particle effect to spawn around
	 *                       the player.
	 */
	@SuppressWarnings("deprecation")
	public static void execute(Player player, String potionEffect, String soundEffect, String particleEffect) {

		// Determine command effects from configuration
		PotionEffectType fxPotionType = isValidPotionEffect(potionEffect) ? PotionEffectType.getByName(potionEffect) : null;
		PotionEffect fxPotion = fxPotionType != null ? new PotionEffect(fxPotionType, 45, 1, false, false, false) : null;
		Sound fxSound = isValidSoundEffect(soundEffect) ? Sound.valueOf(soundEffect) : null;
		Particle fxParticle = isValidParticleEffect(particleEffect) ? Particle.valueOf(particleEffect) : null;

		// Spawn circle of bogged entities
		double radius = 2.0; // adjust this to change the size of the circle
		double angleIncrement = 2 * Math.PI / 6; // 6 entities, so 360 degrees / 6 = 60 degrees per entity

		// Get player location
		Location playerLocation = player.getLocation();

		// Spawn all entities
		final Entity[] entities = new Entity[6];
		for (int i = 0; i < 6; i++) {
			double angle = i * angleIncrement;
			double x = player.getLocation().getX() + radius * Math.cos(angle);
			double z = player.getLocation().getZ() + radius * Math.sin(angle);
			Location entityLocation = new Location(player.getWorld(), x, player.getLocation().getY(), z);

			// Spawn entity
			Entity entity = player.getWorld().spawnEntity(entityLocation, EntityType.BOGGED);

			// Cast entity to living entity
			LivingEntity livingEntity = (LivingEntity) entity;

			// Set entity attributes
			livingEntity.setAI(false);
			livingEntity.setInvulnerable(true);
			livingEntity.setVisualFire(false);

			// Prevent fire damage
			livingEntity.setFireTicks(0);

			// Initially hide the entity from other players
			for (Player otherPlayer : player.getWorld().getPlayers()) {
				if (!otherPlayer.equals(player)) {
					otherPlayer.hideEntity(Main.getInstance(), livingEntity);
				}
			}

			entities[i] = entity;
		}

		// Repeatedly update entities' positions and orientations
		player.getServer().getScheduler().runTaskTimer(Main.getInstance(), () -> {
			for (int i = 0; i < 6; i++) {
				double angle = i * angleIncrement;
				double x = playerLocation.getX() + radius * Math.cos(angle);
				double z = playerLocation.getZ() + radius * Math.sin(angle);
				Location entityLocation = new Location(playerLocation.getWorld(), x, playerLocation.getY(), z);

				LivingEntity livingEntity = (LivingEntity) entities[i];
				livingEntity.teleport(entityLocation);

				// Make entity face the player
				double dx = playerLocation.getX() - entityLocation.getX();
				double dz = playerLocation.getZ() - entityLocation.getZ();
				double yaw = Math.atan2(dz, dx);
				float yawFloat = (float) Math.toDegrees(yaw) - 90; // Adjust yaw to face player

				livingEntity.setRotation(yawFloat, 0); // Set rotation to face the player
			}
		}, 0, 1); // Run every tick (20 times per second)

		// Repeatedly set fireTicks to 0 to prevent burning
		player.getServer().getScheduler().runTaskTimer(Main.getInstance(), () -> {
			for (Entity entity : entities) {
				if (entity instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) entity;
					livingEntity.setFireTicks(0); // Ensure the entity does not burn
				}
			}
		}, 0, 1); // Run every tick (20 times per second)

		// Remove entities after 2 seconds
		player.getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
			for (Entity entity : entities) {
				if (entity != null && !entity.isDead()) {
					entity.remove();
				}
			}
		}, 40); // 2 seconds later

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
