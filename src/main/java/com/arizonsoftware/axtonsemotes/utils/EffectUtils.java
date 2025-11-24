package com.arizonsoftware.axtonsemotes.utils;

import com.arizonsoftware.axtonsemotes.AxtonsEmotes;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.jetbrains.annotations.Nullable;

public class EffectUtils {

   private EffectUtils() {
      throw new IllegalStateException("Utility class");
   }

   /**
    * Parses a particle string into a {@link Particle}. Falls back to the default particle
    * configured in {@code config.yml} if the original is invalid.
    *
    * @param particleString The particle string to parse.
    * @param commandLabel   The emote command label for logging purposes.
    * @return The parsed {@link Particle}, the default if original is invalid, or {@code null} if none valid.
    */
   @Nullable
   public static Particle parseParticleWithDefault(String particleString, String commandLabel) {
      if (particleString != null && !particleString.isEmpty() && !particleString.equalsIgnoreCase("none")) {
         try {
            return Particle.valueOf(particleString);
         } catch (IllegalArgumentException e) {
            String defaultParticle = Configuration.getString("config.yml", "default-effects.particle");
            if (defaultParticle != null && !defaultParticle.isEmpty() && !defaultParticle.equalsIgnoreCase("none")) {
               try {
                  Particle fallbackParticle = Particle.valueOf(defaultParticle);
                  AxtonsEmotes.getInstance().getLogger().warning(
                        "Invalid particle '" + particleString + "' for emote '" + commandLabel +
                              "'. Using default particle: " + defaultParticle);
                  Debugging.log("EffectUtils",
                        "Using default particle '" + defaultParticle + "' for emote '" + commandLabel +
                              "' (original: '" + particleString + "')");
                  return fallbackParticle;
               } catch (IllegalArgumentException ex) {
                  AxtonsEmotes.getInstance().getLogger().severe(
                        "Invalid particle '" + particleString + "' for emote '" + commandLabel +
                              "' and default particle '" + defaultParticle
                              + "' is also invalid. Disabling particle effect.");
                  Debugging.log("EffectUtils",
                        "Both original particle '" + particleString + "' and default particle '" + defaultParticle +
                              "' invalid for emote '" + commandLabel + "'");
                  return null;
               }
            } else {
               AxtonsEmotes.getInstance().getLogger().warning(
                     "Invalid particle '" + particleString + "' for emote '" + commandLabel +
                           "' and no valid default configured. Disabling particle effect.");
               Debugging.log("EffectUtils",
                     "No valid default particle configured for emote '" + commandLabel + "'");
               return null;
            }
         }
      } else {
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
   public static Sound parseSoundWithDefault(String soundString, String commandLabel) {
      if (soundString != null && !soundString.isEmpty() && !soundString.equalsIgnoreCase("none")) {
         try {
            return Sound.valueOf(soundString);
         } catch (IllegalArgumentException e) {
            String defaultSound = Configuration.getString("config.yml", "default-effects.sound");
            if (defaultSound != null && !defaultSound.isEmpty() && !defaultSound.equalsIgnoreCase("none")) {
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
                              "' and default sound '" + defaultSound + "' is also invalid. Disabling sound effect.");
                  Debugging.log("EffectUtils",
                        "Both original sound '" + soundString + "' and default sound '" + defaultSound +
                              "' invalid for emote '" + commandLabel + "'");
                  return null;
               }
            } else {
               AxtonsEmotes.getInstance().getLogger().warning(
                     "Invalid sound '" + soundString + "' for emote '" + commandLabel +
                           "' and no valid default configured. Disabling sound effect.");
               Debugging.log("EffectUtils",
                     "No valid default sound configured for emote '" + commandLabel + "'");
               return null;
            }
         }
      } else {
         return null;
      }
   }
}
