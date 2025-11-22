package com.arizonsoftware.lib.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.arizonsoftware.Main;
import com.arizonsoftware.utils.MessageHandler;
import com.arizonsoftware.utils.Versioning;

public class PlayerEventsListener implements Listener {

    /**
     * Handles the player join event. If version checking is
     * enabled and the plugin is outdated, it notifies the
     * player with the current version, latest version, and a
     * download link.
     * 
     * @param event The PlayerJoinEvent representing the player
     *              joining.
     */
    @EventHandler
    public void function(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Main.getInstance().getConfig().getBoolean("check-for-latest") && Versioning.isLatest() == false) {
            if (Main.getInstance().getConfig().getBoolean("notify-on-update") && player.hasPermission("axtonsemotes.updatenotify")) {
                player.sendMessage(MessageHandler.parseColor("&6AxtonsEmotes &lv" + Versioning.getCurrent() + "&r&6 is outdated! Download &lv" + Versioning.getLatestVersion() + "&r&6 from &ohttps://github.com/axtonprice/axtons-emotes/releases/"));
            }

        }
    }

}
