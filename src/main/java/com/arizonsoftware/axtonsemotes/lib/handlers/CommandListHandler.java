package com.arizonsoftware.axtonsemotes.lib.handlers;

import com.arizonsoftware.axtonsemotes.utils.Configuration;
import com.arizonsoftware.axtonsemotes.utils.MessageHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class CommandListHandler {

   // Number of commands/emotes displayed per page in the paginated list. 
   private static final int COMMANDS_PER_PAGE = 8;

   private CommandListHandler() {
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
      Set<String> emoteNames = Configuration.getConfigurationSectionKeys("emotes.yml", "commands");
      Iterator<String> iterator = emoteNames.iterator();

      while (iterator.hasNext()) {
         String emoteName = iterator.next();
         Boolean enabled = Configuration.getBoolean("emotes.yml", "commands." + emoteName + ".enabled");
         if (!Boolean.TRUE.equals(enabled))
            continue;

         String configEmoteType = Configuration.getString("emotes.yml", "commands." + emoteName + ".type");
         if (configEmoteType == null || configEmoteType.isEmpty()) {
            configEmoteType = "shared";
         }

         if (emoteType.equalsIgnoreCase(configEmoteType)) {
            String commandFormat;
            switch (configEmoteType.toLowerCase()) {
               case "expression" -> commandFormat = "&8- &7Broadcasts to all players";
               case "shared" -> commandFormat = "&7<player>";
               default -> commandFormat = "";
            }
            emotesList.add(new String[] { emoteName, commandFormat });
         }
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
      List<String[]> commandsList = new ArrayList<>();
      Map<String, Command> commands;

      try {
         Object commandMap = Bukkit.getServer()
               .getClass()
               .getMethod("getCommandMap")
               .invoke(Bukkit.getServer());

         @SuppressWarnings("unchecked")
         Map<String, Command> knownCommands = (Map<String, Command>) commandMap
               .getClass()
               .getMethod("getKnownCommands")
               .invoke(commandMap);

         commands = knownCommands;
      } catch (Exception e) {
         throw new RuntimeException("Failed to access Bukkit CommandMap", e);
      }

      Map<String, Command> uniqueCommands = new HashMap<>();
      for (Entry<String, Command> entry : commands.entrySet()) {
         Command cmd = entry.getValue();
         if (uniqueCommands.containsKey(cmd.getName()))
            continue;

         if (cmd instanceof PluginCommand pluginCommand) {
            CommandExecutor executor = pluginCommand.getExecutor();
            if (executorClass.isInstance(executor)) {
               String description = cmd.getDescription() != null ? cmd.getDescription()
                     : MessageHandler.get("command.list.no_description");
               String emoteType = Configuration.getString("emotes.yml", "commands." + cmd.getName() + ".type");
               if (emoteType == null || emoteType.isEmpty())
                  emoteType = "shared";

               String typeIndicator;
               switch (emoteType.toLowerCase()) {
                  case "expression" -> typeIndicator = "&b[Expression]&r ";
                  case "shared" -> typeIndicator = "&e[Shared]&r ";
                  default -> typeIndicator = "&7[Unknown]&r ";
               }

               commandsList.add(new String[] { cmd.getName(), typeIndicator + description });
            }
         }

         uniqueCommands.put(cmd.getName(), cmd);
      }

      return commandsList;
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
    * @param subTitle The subtitle of the paginated list.
    * @param baseCommand The base command used for navigation.
    * @param note Optional note to display on the first page.
    */
   public static void sendPaginatedMessage(int pageCount, String senderName, CommandSender sender,
         List<String[]> commandsList, String title, String subTitle, String baseCommand, String note) {
      int totalPages = (int) Math.ceil((double) commandsList.size() / COMMANDS_PER_PAGE);
      if (pageCount > totalPages)
         pageCount = totalPages;
      if (pageCount < 1)
         pageCount = 1;

      int startIndex = (pageCount - 1) * COMMANDS_PER_PAGE;
      int endIndex = Math.min(startIndex + COMMANDS_PER_PAGE, commandsList.size());

      sender.sendMessage("\n");
      sender.sendMessage(
            MessageHandler.parseColor("&6--- " + title + " --- &7(Page " + pageCount + " of " + totalPages + ")"));
      sender.sendMessage(MessageHandler.parseColor(subTitle));

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
