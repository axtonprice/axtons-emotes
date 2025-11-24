package com.arizonsoftware.axtonsemotes.lib.handlers;

import com.arizonsoftware.axtonsemotes.utils.Debugging;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class BaseHandler {

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
            player.getWorld().spawnParticle(this.particle, player.getLocation(), 3, 0.5D, 0.5D, 0.5D, 0.0D);
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
