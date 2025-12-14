package com.arizonsoftware.axtonsemotes.lib.handlers;

import com.arizonsoftware.axtonsemotes.utils.Configuration;
import com.arizonsoftware.axtonsemotes.utils.MessageHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;

public class EmotesListHandler {

    // Number of commands/emotes displayed per page in the paginated list. 
    private static final int COMMANDS_PER_PAGE = 6;

    private EmotesListHandler() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
    * Retrieves a list of emotes of the specified type for a given sender.
    *
    * @param emoteType The type of emotes to retrieve ("shared" or "expression").
    * @param sender The command sender (used to filter permissions if needed).
    * @return A list of string arrays, where each array contains the emote name and a description or argument format.
    */
    public static List<String[]> getEmotesList(String emoteType, CommandSender sender) {
        List<String[]> emotesList = new ArrayList<>();

        // All emotes now come from the same section
        String sectionPath = "shared-emotes";

        // Validate type
        String type = emoteType.toLowerCase();
        if (!type.equals("expression") && !type.equals("shared"))
            return emotesList;

        // Fetch emote names
        Set<String> emoteNames = Configuration.getConfigSectionKeys("emotes.yml", sectionPath);
        if (emoteNames == null || emoteNames.isEmpty())
            return emotesList;

        for (String emoteName : emoteNames) {

            // Enabled?
            Boolean enabled = Configuration.getBoolean("emotes.yml", sectionPath + "." + emoteName + ".enabled");
            if (!Boolean.TRUE.equals(enabled))
                continue;

            // Different command formats depending on type
            String commandFormat = switch (type) {
                case "expression" -> "&8- &7Broadcasts to all players";
                case "shared" -> "&7<player>";
                default -> "";
            };

            emotesList.add(new String[] { emoteName, commandFormat });
        }

        return emotesList;
    }

    /**
    * Retrieves a list of commands whose executor matches the provided class type.
    *
    * @param executorClass The class type to filter command executors.
    * @return A list of string arrays containing command names and formatted descriptions.
    */
    public static List<String[]> getCommandsList(Class<? extends CommandExecutor> executorClass) {
        Map<String, Command> commands;

        // Access CommandMap via reflection 
        try {
            Object server = Bukkit.getServer();
            Object commandMap = server.getClass().getMethod("getCommandMap").invoke(server);

            @SuppressWarnings("unchecked")
            Map<String, Command> knownCommands = (Map<String, Command>) commandMap.getClass()
                    .getMethod("getKnownCommands")
                    .invoke(commandMap);

            commands = knownCommands;
        } catch (Exception e) {
            throw new RuntimeException("Failed to access Bukkit CommandMap", e);
        }

        // Load emotes.yml once, not per command 
        YamlConfiguration emotes = Configuration.getConfig("emotes.yml");

        List<String[]> results = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (Command cmd : commands.values()) {

            // Skip duplicates
            if (!seen.add(cmd.getName()))
                continue;

            // Only PluginCommand and only matching executor
            if (!(cmd instanceof PluginCommand pc))
                continue;
            if (!executorClass.isInstance(pc.getExecutor()))
                continue;

            // Description 
            String description = cmd.getDescription();
            if (description == null || description.isEmpty())
                description = MessageHandler.get("command.list.no_description");

            // Emote type 
            String emoteType = "shared"; // default
            if (emotes != null) {
                if (emotes.isSet("expressions." + cmd.getName()))
                    emoteType = "expression";
                else if (emotes.isSet("shared-emotes." + cmd.getName()))
                    emoteType = "shared";
            }

            // Type indicator 
            String typeIndicator = switch (emoteType) {
                case "expression" -> "&b[Expression]&r ";
                case "shared" -> "&e[Shared]&r ";
                default -> "&7[Unknown]&r ";
            };

            results.add(new String[] { cmd.getName(), typeIndicator + description });
        }

        return results;
    }

    /**
    * Sends a paginated message to the command sender.
    *
    * @param pageCount The page number to display.
    * @param senderName The name of the sender (for logging or display purposes).
    * @param sender The command sender to send messages to.
    * @param commandsList The list of commands/emotes to display.
    * @param title The title of the paginated list.
    * @param subTitle The subtitle of the paginated list.
    * @param baseCommand The base command used for navigation.
    */
    public static void sendPaginatedMessage(int pageCount, String senderName, CommandSender sender,
            List<String[]> commandsList, String title, String subTitle, String baseCommand) {
        sendPaginatedMessage(pageCount, senderName, sender, commandsList, title, subTitle, baseCommand, null);
    }

    /**
    * Sends a paginated message to the command sender with an optional note on the first page.
    *
    * @param pageCount The page number to display.
    * @param senderName The name of the sender (for logging or display purposes).
    * @param sender The command sender to send messages to.
    * @param commandsList The list of commands/emotes to display.
    * @param title The title of the paginated list.
    * @param description The subtitle of the paginated list.
    * @param baseCommand The base command used for navigation.
    * @param note Optional note to display on the first page.
    */
    public static void sendPaginatedMessage(int pageCount, String senderName, CommandSender sender,
            List<String[]> commandsList, String title, String description, String baseCommand, String note) {
        int totalPages = (int) Math.ceil((double) commandsList.size() / COMMANDS_PER_PAGE);
        if (pageCount > totalPages)
            pageCount = totalPages;
        if (pageCount < 1)
            pageCount = 1;

        int startIndex = (pageCount - 1) * COMMANDS_PER_PAGE;
        int endIndex = Math.min(startIndex + COMMANDS_PER_PAGE, commandsList.size());

        sender.sendMessage("\n");
        sender.sendMessage(MessageHandler.parseColor("&8- &b&lAxtonsEmotes &8-"));
        sender.sendMessage(
                MessageHandler
                        .parseColor("&7" + MessageHandler.get("command.list.navigation.listing") + ": &6" + title));
        sender.sendMessage(
                MessageHandler.parseColor("&7" + MessageHandler.parseReplace("command.list.navigation.page",
                        new String[] { "current_page", "total_pages" },
                        new String[] { MessageHandler.parseColor("&9" + String.valueOf(pageCount)),
                                MessageHandler.parseColor("&9" + String.valueOf(totalPages)) })));
        sender.sendMessage("\n");
        sender.sendMessage(MessageHandler.parseColor(description));

        if (note != null && !note.trim().isEmpty() && pageCount == 1) {
            sender.sendMessage(MessageHandler.parseColor(note));
        }

        sender.sendMessage("\n");

        for (int i = startIndex; i < endIndex; ++i) {
            String[] entry = commandsList.get(i);
            String emoteName = entry[0];
            String args = entry[1];
            if (args != null && !args.trim().isEmpty()) {
                sender.sendMessage(MessageHandler.parseColor("&e/e " + emoteName + " " + args));
            } else {
                sender.sendMessage(MessageHandler.parseColor("&e/e " + emoteName));
            }
        }

        if (pageCount < totalPages) {
            sender.sendMessage("\n");
            String pageNumber = String.valueOf(pageCount + 1);
            String[] subjects = new String[] { "command_name", "page_number" };
            String[] replacements = new String[] { baseCommand, pageNumber };
            sender.sendMessage(MessageHandler.get("command.list.navigation.footer", subjects, replacements));
        }

        if (pageCount == totalPages) {
            sender.sendMessage("\n");
        }
    }
}
