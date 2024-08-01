package com.arizonsoftware.commands.misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arizonsoftware.utils.Strings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AxtonsEmotes implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("emotes")) {
                ListEmotes ListEmotes = new ListEmotes();
                ListEmotes.exe(sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("expressions")) {
                ListExpressions ListExpressions = new ListExpressions();
                ListExpressions.exe(sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                new ReloadPlugin(sender);
                return true;
            }
        } else {
            sender.sendMessage(Strings.ParseColors(
                    "§4Error: §cSpecify a valid emote or expression! Use /emotes or /expressions to list available commands."));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {

        if (args.length == 1)
            return getTabComplete(args, "emotes", "expressions", "reload");
        return null;
    }

    public static List<String> getTabComplete(String[] args, String... completes) {
        List<String> strings = Arrays.asList(completes);
        if (args.length == 0)
            return strings;
        return strings.stream().filter(s -> s.startsWith(args[args.length - 1])).collect(Collectors.toList());
    }
}
