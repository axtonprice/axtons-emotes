package com.arizonsoftware.commands;

import org.bukkit.command.CommandSender;

import com.arizonsoftware.Main;
import com.arizonsoftware.utils.Versioning;
import com.arizonsoftware.utils.MessageHandler;

public class VersionCommand {

    /**
     * Represents a command that displays the version
     * information of the plugin.
     *
     * @param sender The command sender.
     */
    public VersionCommand(CommandSender sender) {

        // Execution validation
        if (!sender.hasPermission("axtonsemotes.version")) {
            sender.sendMessage(MessageHandler.parseColor(MessageHandler.get("message_error_player_permission")));
            return;
        }

        // Output message to sender
        String message = MessageHandler.get("message_context_command_version.command");
        String[] subjects = { "current_version", "latest_version", "repository_url" };
        String[] replacements = { Versioning.current, Versioning.getLatest(), Main.getInstance().getPluginMeta().getWebsite() };
        sender.sendMessage(MessageHandler.format(message, subjects, replacements, true));

    }
}