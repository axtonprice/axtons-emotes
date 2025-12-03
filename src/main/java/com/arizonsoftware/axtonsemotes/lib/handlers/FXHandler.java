package com.arizonsoftware.axtonsemotes.lib.handlers;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import com.arizonsoftware.axtonsemotes.utils.Configuration;
import com.arizonsoftware.axtonsemotes.utils.Debugging;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FXHandler {

   // The command that initiated this handler. 
   protected static Command thisCommand;

   // The sender who executed the command. 
   protected static CommandSender thisSender = null;

   // Particle effect to use in the emote. 
   protected Particle particle = null;

   // Sound effect to use in the emote. 
   protected Sound sound = null;

   /**
    * Initializes the static command and sender references for the handler.
    * Should be called before executing any emote.
    *
    * @param command The command that triggered the handler.
    * @param sender The sender who executed the command.
    */
   public static void initialize(Command command, CommandSender sender) {
      thisCommand = command;
      thisSender = sender;
   }

   /**
   * Converts a particle string into a {@link Particle}. Falls back to the default particle
   * configured in {@code config.yml} if the original is invalid.
   *
   * @param particleString The particle string to convert.
   * @param commandLabel   The emote command label for logging purposes.
   * @return The converted {@link Particle}, the default if original is invalid, or {@code null} if none valid.
   */
   @Nullable
   public static Particle convertParticle(String particleString, String commandLabel) {

      // Check for "none" or empty input 
      if (particleString == null || particleString.isEmpty() || particleString.equalsIgnoreCase("none")) {
         return null;
      }

      // Attempt to convert user-specified particle
      try {
         return Particle.valueOf(particleString);
      } catch (IllegalArgumentException ignored) {
         // Proceed to fallback logic
      }

      // Fetch default particle from config
      String defaultParticle = Configuration.getString("config.yml", "default-effects.particle");

      // If no default configured or default is "none", we stop here
      if (defaultParticle == null || defaultParticle.isEmpty() || defaultParticle.equalsIgnoreCase("none")) {
         AxtonsEmotes.getInstance().getLogger().warning(
               "Invalid particle '" + particleString + "' for emote '" + commandLabel +
                     "', and no valid default configured. Disabling particle effect.");
         Debugging.log("EffectUtils",
               "No valid default particle configured for emote '" + commandLabel + "'");
         return null;
      }

      // Attempt to use default fallback particle
      try {
         Particle fallback = Particle.valueOf(defaultParticle);

         AxtonsEmotes.getInstance().getLogger().warning(
               "Invalid particle '" + particleString + "' for emote '" + commandLabel +
                     "'. Using default particle: " + defaultParticle);
         Debugging.log("EffectUtils",
               "Using default particle '" + defaultParticle + "' for emote '" + commandLabel +
                     "' (original: '" + particleString + "')");

         return fallback;

      } catch (IllegalArgumentException ex) {
         AxtonsEmotes.getInstance().getLogger().severe(
               "Invalid particle '" + particleString + "' for emote '" + commandLabel +
                     "' — and default '" + defaultParticle +
                     "' is also invalid. Disabling particle effect.");

         Debugging.log("EffectUtils",
               "Both original particle '" + particleString + "' and default '" + defaultParticle +
                     "' invalid for emote '" + commandLabel + "'");

         return null;
      }
   }

   /**
   * Parses a sound string into a {@link Sound}. Falls back to the default sound
   * configured in {@code config.yml} if the original is invalid.
   *
   * @param soundString  The sound string to parse.
   * @param commandLabel The emote command label for logging purposes.
   * @return The parsed {@link Sound}, the default if original is invalid, or {@code null} if none valid.
   */
   @Nullable
   public static Sound convertSound(String soundString, String commandLabel) {

      // Check for "none" or empty input 
      if (soundString == null || soundString.isEmpty() || soundString.equalsIgnoreCase("none")) {
         return null;
      }

      // Attempt to parse user-specified sound
      try {
         return Sound.valueOf(soundString);
      } catch (IllegalArgumentException ignored) {
      }

      // Fetch default sound from config
      String defaultSound = Configuration.getString("config.yml", "default-effects.sound");

      // If no fallback available or is "none"
      if (defaultSound == null || defaultSound.isEmpty() || defaultSound.equalsIgnoreCase("none")) {
         AxtonsEmotes.getInstance().getLogger().warning(
               "Invalid sound '" + soundString + "' for emote '" + commandLabel +
                     "' and no valid default configured. Disabling sound effect.");
         Debugging.log("EffectUtils",
               "No valid default sound configured for emote '" + commandLabel + "'");
         return null;
      }

      // Attempt to parse fallback default sound
      try {
         Sound fallbackSound = Sound.valueOf(defaultSound);

         AxtonsEmotes.getInstance().getLogger().warning(
               "Invalid sound '" + soundString + "' for emote '" + commandLabel +
                     "'. Using default sound: " + defaultSound);
         Debugging.log("EffectUtils",
               "Using default sound '" + defaultSound + "' for emote '" + commandLabel +
                     "' (original: '" + soundString + "')");
         return fallbackSound;

      } catch (IllegalArgumentException ex) {
         AxtonsEmotes.getInstance().getLogger().severe(
               "Invalid sound '" + soundString + "' for emote '" + commandLabel +
                     "' and default sound '" + defaultSound +
                     "' is also invalid. Disabling sound effect.");
         Debugging.log("EffectUtils",
               "Both original sound '" + soundString + "' and default sound '" + defaultSound +
                     "' invalid for emote '" + commandLabel + "'");
         return null;
      }
   }

   /**
    * Sets the particle and sound effects for this emote.
    * Rejects unsupported particle effects such as CLOUD.
    *
    * @param particle The particle effect to use.
    * @param sound The sound effect to use.
    * @return true if the particle effect is valid and applied; false if unsupported.
    */
   public boolean setFX(Particle particle, Sound sound) {
      if (particle == Particle.CLOUD) {
         Logger logger = Bukkit.getLogger();
         logger.severe("[AxtonsEmotes] [!] Unsupported particle effect: " + particle + " in emote command: "
               + thisCommand.getLabel());
         this.particle = null;
         this.sound = sound;
         return false;
      } else {
         this.particle = particle;
         this.sound = sound;
         return true;
      }
   }

   /**
    * Emits the configured particle effect at the player's location.
    * Safely handles invalid or unsupported particle IDs.
    *
    * @param player The player at whose location the particle should appear.
    */
   protected void emitParticle(Player player) {
      if (this.particle != null) {
         try {
            player.getWorld().spawnParticle(this.particle, player.getLocation(), 5, 0.5D, 1D, 0.5D, 0.0D);
         } catch (IllegalArgumentException e) {
            Debugging.log(this.getClass().getSimpleName() + "/" +
                  Thread.currentThread().getStackTrace()[1].getMethodName(),
                  "Invalid or unsupported particle ID: " + e.getMessage());
         }
      }
   }

   /**
    * Plays the configured sound effect at the player's location.
    * Safely handles invalid or unsupported sound IDs.
    *
    * @param player The player at whose location the sound should be played.
    */
   protected void emitSound(Player player) {
      if (this.sound != null) {
         try {
            player.getWorld().playSound(player.getLocation(), this.sound, 1.0F, 1.0F);
         } catch (IllegalArgumentException e) {
            Debugging.log(this.getClass().getSimpleName() + "/" +
                  Thread.currentThread().getStackTrace()[1].getMethodName(),
                  "Invalid or unsupported sound ID: " + e.getMessage());
         }
      }
   }

   /**
    * Executes the emote logic.
    * Must be implemented by subclasses to define specific emote behavior.
    *
    * @param args Arguments passed to the emote command.
    * @param command The command object that triggered this handler.
    */
   public abstract void execute(@NotNull String[] args, @NotNull Command command);
}
