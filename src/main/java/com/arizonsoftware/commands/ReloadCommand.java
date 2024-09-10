package com.arizonsoftware.commands;

import org.bukkit.command.CommandSender;

import com.arizonsoftware.Main;
import com.arizonsoftware.utils.Registry;
import com.arizonsoftware.utils.MessageHandler;

public class ReloadCommand {

    /**
     * Represents a command that reloads the plugin.
     *
     * @param sender The command sender.
     */
    public ReloadCommand(CommandSender sender) {

        // Execution validation
        if (!sender.hasPermission("axtonsemotes.reload")) {
            sender.sendMessage(MessageHandler.parseColor(MessageHandler.get("message_error_player_permission")));
            return;
        }

        // Timings variables
        long startTime = System.currentTimeMillis();

        // Reload plugin
        Main instance = Main.getInstance();
        instance.reloadConfig();

        // Re-register commands and listeners
        Registry.registerAll(startTime);

        // Output message to sender
        String message = MessageHandler.get("message_context_command_reload");
        String[] subjects = { "exec_time" };
        String[] replacements = { String.valueOf(System.currentTimeMillis() - startTime) };
        sender.sendMessage(MessageHandler.format(message, subjects, replacements, true));

    }
}