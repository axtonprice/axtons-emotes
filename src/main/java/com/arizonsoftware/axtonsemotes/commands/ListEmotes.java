package com.arizonsoftware.axtonsemotes.commands;

import com.arizonsoftware.axtonsemotes.lib.handlers.CommandListHandler;
import com.arizonsoftware.axtonsemotes.utils.Configuration;
import com.arizonsoftware.axtonsemotes.utils.Debugging;
import com.arizonsoftware.axtonsemotes.utils.MessageHandler;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Command executor for listing emotes or expressions.
 * Handles permissions, configuration restrictions, and paginated output.
 */
public class ListEmotes implements CommandExecutor {

   @NotNull
   private String thisLabel = "emotes";

   private final String emoteType;

   /**
    * Constructs a ListEmotes command for a specific emote type.
    *
    * @param emoteType The type of emotes to list (e.g., "shared" or "expression").
    */
   public ListEmotes(String emoteType) {
      this.emoteType = emoteType;
   }

   /**
    * Executes the command when called by the Bukkit command system.
    *
    * @param sender The sender of the command (player or console).
    * @param command The command object that was executed.
    * @param label The command label used.
    * @param args Arguments passed to the command (e.g., page number).
    * @return true if command executed without error.
    */
   @Override
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
         @NotNull String[] args) {
      this.thisLabel = command.getLabel();
      this.execute(sender, args);
      return true;
   }

   /**
    * Handles the actual listing logic for emotes or expressions.
    *
    * Performs checks for:
    * - Sender type (player only)
    * - Permission to list emotes
    * - Configuration restrictions (allow-list-commands)
    *
    * @param sender The command sender (player or console).
    * @param args Arguments passed to the command, where args[0] may be the page number.
    */
   public void execute(CommandSender sender, @NotNull String[] args) {
      String logContext;

      // Check if sender is a player
      if (!(sender instanceof Player)) {

         logContext = this.getClass().getSimpleName();
         Debugging.log(logContext + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
               "List command attempt failed: Sender is not a player (Console)");
         sender.sendMessage(MessageHandler.parseError("error.player.only"));

      } else if (!sender.hasPermission("axtonsemotes.emotes.list")) {

         logContext = this.getClass().getSimpleName();
         Debugging.log(logContext + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
               "List command attempt failed: No permission to list emotes by " + sender.getName());
         sender.sendMessage(MessageHandler.parseError("error.permission"));

      } else {
         // Check configuration for allow-list-commands
         Boolean allowListCommands = Configuration.getBoolean("config.yml", "allow-list-commands");

         if (Boolean.FALSE.equals(allowListCommands) && !sender.hasPermission("axtonsemotes.admin.list-override")
               && !sender.isOp()) {

            logContext = this.getClass().getSimpleName();
            Debugging.log(logContext + "/" + Thread.currentThread().getStackTrace()[1].getMethodName(),
                  "List command attempt denied: " + sender.getName() + " - allow-list-commands is disabled");
            sender.sendMessage(MessageHandler.parseError("error.command.disabled"));

         } else {
            // Determine page number
            int pageCount = 1;
            if (args.length > 0) {
               try {
                  pageCount = Integer.parseInt(args[0]);
               } catch (NumberFormatException ignored) {
                  pageCount = 1;
               }
            }

            // Retrieve emotes list
            List<String[]> emotesList = CommandListHandler.getEmotesList(this.emoteType, sender);

            // Debug logging
            logContext = this.getClass().getSimpleName() + "/"
                  + Thread.currentThread().getStackTrace()[1].getMethodName();
            Debugging.log(logContext, "Determined page number " + pageCount);
            Debugging.log(logContext, "Counted " + emotesList.size() + " " + this.emoteType + " emotes");

            // Prepare titles and notes
            String title;
            String subTitle;
            String note;

            // Determine titles and notes based on emote type
            if ("expression".equalsIgnoreCase(this.emoteType)) {
               title = MessageHandler.get("command.list.titles.expressions");
               subTitle = MessageHandler.get("command.list.subtitles.expressions");
               note = MessageHandler.get("command.list.notes.expressions");
            } else {
               title = MessageHandler.get("command.list.titles.emotes");
               subTitle = MessageHandler.get("command.list.subtitles.emotes");
               note = MessageHandler.get("command.list.notes.emotes");
            }

            // Send paginated message
            if (sender instanceof Player player) {
               CommandListHandler.sendPaginatedMessage(pageCount, player.getName(), sender, emotesList, title, subTitle,
                     this.thisLabel, note);
            } else {
               CommandListHandler.sendPaginatedMessage(pageCount, "Console", sender, emotesList, title, subTitle,
                     this.thisLabel, note);
            }
         }
      }
   }
}
