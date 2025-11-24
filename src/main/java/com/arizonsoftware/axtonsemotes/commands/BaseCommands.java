package com.arizonsoftware.axtonsemotes.commands;

import com.arizonsoftware.axtonsemotes.utils.MessageHandler;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseCommands implements TabExecutor {
   private final UtilityCommands AECommands = new UtilityCommands();

   /**
    * Handle incoming commands and route subcommands to {@code UtilityCommands}.
    *
    * @param sender the sender who executed the command
    * @param command the {@link Command} instance executed
    * @param label the command label used (alias or primary name)
    * @param args the command arguments; the first argument (if present) is treated as the subcommand
    * @return {@code true} if a known subcommand was handled, {@code false} otherwise
    */
   @Override
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
         @NotNull String[] args) {

      // Validate presence of subcommand
      if (args.length == 0) {
         sender.sendMessage(MessageHandler.parseError("error.command.syntax.sub_command"));
         return false;
      }

      // Extract subcommand
      String subcommand = args[0].toLowerCase();

      // Route to appropriate subcommand handler
      switch (subcommand) {
         case "toggledebug":
            this.AECommands.toggleConfig("debug-mode.enabled", subcommand, sender);
            return true;

         case "togglemetrics":
            this.AECommands.toggleConfig("enable-metrics", subcommand, sender);
            return true;

         case "reload":
            this.AECommands.reloadConfiguration(sender);
            return true;

         case "version":
            this.AECommands.pluginVersion(subcommand, sender);
            return true;

         case "resetlang":
            this.AECommands.resetLangFiles(sender);
            return true;

         case "help":
            this.AECommands.helpCommand(sender);
            return true;

         default:
            sender.sendMessage(MessageHandler.parseError("error.command.syntax.sub_command"));
            return false;
      }
   }

   /**
    * Provide tab completions for the command. Only completes the first argument in this implementation.
    *
    * @param sender the sender requesting completions
    * @param command the {@link Command} instance for which completions are requested
    * @param label the command label used
    * @param args the current arguments typed by the sender; last element is used as the completion prefix
    * @return a list of completion candidates when applicable, or {@code null} when none
    */
   @Override
   @Nullable
   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
         @NotNull String[] args) {

      // Only complete the first argument
      if (args.length != 1)
         return null;

      // Define possible subcommands and their required permissions
      String[] commands = {
            "toggledebug",
            "togglemetrics",
            "reload",
            "version",
            "resetlang",
            "help"
      };
      String[] permissions = {
            "axtonsemotes.admin.toggledebug",
            "axtonsemotes.admin.togglemetrics",
            "axtonsemotes.admin.reload",
            "axtonsemotes.admin.version",
            "axtonsemotes.admin.resetlang",
            "axtonsemotes.admin.help"
      };

      return this.getTabCompletions(sender, args, commands, permissions);
   }

   /**
    * Compute tab-completion candidates filtered by permission and prefix.
    *
    * @param sender the sender requesting completions
    * @param args the current argument list; the last element is the prefix to match
    * @param commands an array of possible command strings to consider
    * @param permissions an array of permissions corresponding to {@code commands}; same ordering is required
    * @return a list of matching commands the sender has permission to use
    */
   private List<String> getTabCompletions(CommandSender sender, String[] args, String[] commands,
         String[] permissions) {

      // Filter commands by sender permissions
      List<String> availableCommands = Arrays.stream(commands)
            .filter((commandStr) -> sender.hasPermission(permissions[Arrays.asList(commands).indexOf(commandStr)]))
            .collect(Collectors.toList());

      // Match available commands against the last argument prefix
      String lastArg = args[args.length - 1].toLowerCase();

      // Filter available commands by prefix matching
      return availableCommands.stream()
            .filter((cmd) -> cmd.startsWith(lastArg))
            .collect(Collectors.toList());
   }
}
