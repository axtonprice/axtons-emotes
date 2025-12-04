package com.arizonsoftware.axtonsemotes.utils;

import org.bukkit.entity.Player;

public class EmoteCooldown {

    /**
     * Checks if emote cooldowns are enabled in the configuration.
     *
     * @return {@code true} if emote cooldowns are enabled; {@code false} otherwise.
     */
    public static boolean isCooldownEnabledConfig() {
        return Configuration.getBoolean("config.yml", "emote-cooldown.enabled");
    }

    /**
     * Checks if the specified player is currently in an emote cooldown period.
     *
     * This method retrieves the cooldown duration from the configuration file and the player's
     * last emote usage timestamp. It then determines if the elapsed time since the last usage
     * is less than the configured cooldown duration.
     *
     * @param player the player to check for cooldown status
     * @return {@code true} if the player is still in cooldown; {@code false} otherwise
     */
    public static boolean isPlayerInCooldown(Player player) {

        // Get cooldown duration from config and check for admin bypass permission
        int cooldownDuration = Configuration.getInt("config.yml", "emote-cooldown.duration-seconds");
        boolean playerImmune = player.hasPermission("axtonsemotes.admin.bypasscooldown");

        // Retrieve stored value as object
        Object value = PlayerData.retrievePlayerSetting("cooldowns", player, Object.class);

        if (value != null && !playerImmune) {
            long lastRunTime;

            try {
                // Case 1: already a number
                if (value instanceof Number) {
                    lastRunTime = ((Number) value).longValue();
                }
                // Case 2: stored as string
                else {
                    lastRunTime = Long.parseLong(value.toString());
                }

                long elapsedSeconds = (System.currentTimeMillis() - lastRunTime) / 1000;
                return elapsedSeconds < cooldownDuration;
            } catch (Exception e) {
                return false; // if corrupted or missing, don't block player
            }
        }

        // No stored value means no cooldown
        return false;
    }

    /**
     * Sets a cooldown for the specified player by storing the current system time as the last run time.
     *
     * @param player the player for whom the cooldown is being set
     */
    public static void setCooldown(Player player) {
        long currentTime = System.currentTimeMillis();
        PlayerData.storePlayerSetting("cooldowns", player, currentTime);
    }

}
