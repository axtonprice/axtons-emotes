package com.arizonsoftware.axtonsemotes.lib.listeners;

import com.arizonsoftware.axtonsemotes.utils.Versioning;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEventsListener implements Listener {

   /**
    * Called when a player joins the server.
    * 
    * Checks if the joining player has permission to receive update notifications and, if so,
    * sends a clickable message with the latest plugin version information.
    *
    * @param event The PlayerJoinEvent representing the player joining the server.
    */
   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      Versioning.notifyPlayer(player);
   }
}
