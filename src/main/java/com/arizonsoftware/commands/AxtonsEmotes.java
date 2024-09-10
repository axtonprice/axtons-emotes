package com.arizonsoftware.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arizonsoftware.utils.MessageHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AxtonsEmotes implements TabExecutor {

    /**
     * Executes the command when it is triggered.
     *
     * @param sender  the command sender
     * @param command the command object
     * @param label   the command label
     * @param args    the command arguments
     * @return true if the command was executed successfully,
     *         false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("emotes")) {
                ListEmotes ListEmotes = new ListEmotes();
                ListEmotes.execute(sender, args);
                return true;
            } else if (args[0].equalsIgnoreCase("expressions")) {
                ListExpressions ListExpressions = new ListExpressions();
                ListExpressions.execute(sender, args);
                return true;
            } else if (args[0].equalsIgnoreCase("jumpscares")) {
                ListJumpscares ListJumpscares = new ListJumpscares();
                ListJumpscares.execute(sender, args);
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                new ReloadCommand(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("version")) {
                new VersionCommand(sender);
                return true;
            } else {
                sender.sendMessage(MessageHandler.parseColor(MessageHandler.get("message_error_command_syntax.sub-command")));
            }
        } else {
            sender.sendMessage(MessageHandler.parseColor(MessageHandler.get("message_error_command_syntax.sub-command")));
        }
        return false;
    }

    /**
     * Provides tab completion for the AxtonsEmotes command.
     *
     * @param sender  the command sender
     * @param command the command being executed
     * @param label   the command label
     * @param args    the command arguments
     * @return a list of tab completions, or null if no
     *         completions are available
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1)
            return getTabComplete(args, "emotes", "expressions", "jumpscares", "reload", "version");
        return null;
    }

    /**
     * Returns a list of tab completions based on the given
     * arguments and completes.
     *
     * @param args      the array of arguments
     * @param completes the array of completions
     * @return a list of tab completions
     */
    public static List<String> getTabComplete(String[] args, String... completes) {
        List<String> strings = Arrays.asList(completes);
        if (args.length == 0)
            return strings;
        return strings.stream().filter(s -> s.startsWith(args[args.length - 1])).collect(Collectors.toList());
    }
}
