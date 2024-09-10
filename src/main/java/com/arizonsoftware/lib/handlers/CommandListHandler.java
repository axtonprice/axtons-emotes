package com.arizonsoftware.lib.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import com.arizonsoftware.utils.MessageHandler;

public class CommandListHandler {

	private static final int COMMANDS_PER_PAGE = 5;

	/**
	 * Retrieves a list of commands that are executed by the
	 * specified executor class.
	 *
	 * @param executorClass The class of the executor.
	 * @return A list of commands and their descriptions.
	 */
	public static List<String[]> getCommandsList(Class<? extends CommandExecutor> executorClass) {
		List<String[]> commandsList = new ArrayList<>();
		Map<String, Command> commands = Bukkit.getCommandMap().getKnownCommands();
		Map<String, Command> uniqueCommands = new HashMap<>();

		for (Map.Entry<String, Command> entry : commands.entrySet()) {
			Command cmd = entry.getValue();

			if (uniqueCommands.containsKey(cmd.getName())) {
				continue;
			}

			if (cmd instanceof PluginCommand) {
				CommandExecutor executor = ((PluginCommand) cmd).getExecutor();
				if (executorClass.isInstance(executor)) {
					String description = cmd.getDescription() != null ? cmd.getDescription() : MessageHandler.get("message_context_command_no_description");
					commandsList.add(new String[] { cmd.getName(), description });
				}
			}

			uniqueCommands.put(cmd.getName(), cmd);
		}
		return commandsList;
	}

	/**
	 * Sends a paginated message to the command sender.
	 *
	 * @param pageCount    The current page number.
	 * @param senderName   The name of the sender.
	 * @param sender       The command sender.
	 * @param commandsList The list of commands.
	 * @param title        The title of the message.
	 * @param baseCommand  The base command for pagination.
	 */
	public static void sendPaginatedMessage(int pageCount, String senderName, CommandSender sender, List<String[]> commandsList, String title, String baseCommand) {
		int totalPages = (int) Math.ceil((double) commandsList.size() / COMMANDS_PER_PAGE);
		if (pageCount > totalPages)
			pageCount = totalPages;
		if (pageCount < 1)
			pageCount = 1;

		int startIndex = (pageCount - 1) * COMMANDS_PER_PAGE;
		int endIndex = Math.min(startIndex + COMMANDS_PER_PAGE, commandsList.size());

		sender.sendMessage("\n");
		sender.sendMessage(MessageHandler.parseColor("&6--- " + title + " --- &7(Page " + pageCount + " of " + totalPages + ")"));
		sender.sendMessage("\n");

		for (int i = startIndex; i < endIndex; i++) {
			String[] command = commandsList.get(i);
			sender.sendMessage(MessageHandler.parseColor("&e/" + command[0] + " &7- " + command[1]));
		}

		if (pageCount < totalPages) {
			sender.sendMessage("\n");
			sender.sendMessage(MessageHandler.parseColor("&7Type &9/" + baseCommand + " " + (pageCount + 1) + "&r&7 to see the next page."));
		}
	}
}