package com.arizonsoftware.commands.misc;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.Main;
import com.arizonsoftware.utils.Validation;

public class BogScare implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        // Execution validation
        if (!Validation.checkIsEnabled(command, sender))
            return true;
        if (!Validation.checkIsPlayer(sender))
            return true;
        if (!Validation.checkHasPermission(sender, command))
            return true;

        // Get player
        final Player player = (Player) sender;

        // Spawn circle of bogged entities
        double radius = 2.0; // adjust this to change the size of the circle
        double angleIncrement = 2 * Math.PI / 6; // 6 entities, so 360 degrees / 6 = 60 degrees per entity

        final Entity[] entities = new Entity[6];

        // Spawn all entities
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
        player.getServer().getScheduler().runTaskTimer(
                Main.getInstance(),
                () -> {
                    Location playerLocation = player.getLocation();
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
        player.getServer().getScheduler().runTaskTimer(
                Main.getInstance(),
                () -> {
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            livingEntity.setFireTicks(0); // Ensure the entity does not burn
                        }
                    }
                }, 0, 1); // Run every tick (20 times per second)

        // Remove entities after 2 seconds
        player.getServer().getScheduler().runTaskLater(
                Main.getInstance(),
                () -> {
                    for (Entity entity : entities) {
                        if (entity != null && !entity.isDead()) {
                            entity.remove();
                        }
                    }
                }, 40); // 2 seconds later

        // Play sound effect
        player.getWorld().playSound(player.getLocation(), Sound.AMBIENT_CAVE, 1, 1);

        // Give player blindness with no particle effects
        player.addPotionEffect(
                new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS, 60, 1, false, false));

        // Output command output
        return true;
    }
}
