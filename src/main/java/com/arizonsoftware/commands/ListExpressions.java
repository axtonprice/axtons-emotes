package com.arizonsoftware.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.arizonsoftware.lib.executors.ExpressionsExecutor;
import com.arizonsoftware.lib.handlers.CommandListHandler;
import com.arizonsoftware.utils.Debugging;
import com.arizonsoftware.utils.MessageHandler;

import java.util.List;

public class ListExpressions implements CommandExecutor {

    private @NotNull String thisLabel = "expressions";

    /**
     * Executes the command when it is triggered.
     *
     * @param sender  the command sender
     * @param command the command object
     * @param label   the command label
     * @param args    the command arguments
     * @return true if the command execution is successful,
     *         false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Set command label variable
        thisLabel = command.getLabel();

        // Execute command
        if (!execute(sender, args))
            sender.sendMessage(MessageHandler.parseColor(MessageHandler.get("message_error_command_failed")));

        return true;
    }

    /**
     * Executes the command to list expressions.
     *
     * @param sender The command sender.
     * @param args   The command arguments.
     * @return true if the command was executed successfully,
     *         false otherwise.
     */
    public boolean execute(CommandSender sender, @NotNull String[] args) {
        // Get pageCount from arguments and verify it is a number.
        int pageCount = 1;
        if (args.length > 0) {
            try {
                pageCount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                pageCount = 1;
            }
        }

        // Retrieve the list of expressions
        List<String[]> expressionsList = CommandListHandler.getCommandsList(ExpressionsExecutor.class);

        // Debug logging
        Debugging.log(this.getClass().getSimpleName(), "Determined page number " + pageCount);
        Debugging.log(this.getClass().getSimpleName(), "Counted " + expressionsList.size() + " commands");

        // Determine sender type and send appropriate message
        if (sender instanceof Player) {
            CommandListHandler.sendPaginatedMessage(pageCount, ((Player) sender).getName(), sender, expressionsList, MessageHandler.get("message_context_command_expressions_title"), thisLabel);
        } else {
            CommandListHandler.sendPaginatedMessage(pageCount, "Console", sender, expressionsList, MessageHandler.get("message_context_command_expressions_title"), thisLabel);
        }

        return true;
    }
}