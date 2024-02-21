package arizonsoftware.axtonsemotes;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class test extends JavaPlugin {

    // Command items array
    private final CommandItem[] commands = {
            new CommandItem(
                    new String[]{"hug", "Hug another player"},
                    new String[]{"cuddle"},
                    new String[]{"hugged"}, // past tense verbs, etc
                    new String[]{"c"}, // colour
                    new Particle[]{Particle.VILLAGER_ANGRY},
                    new Sound[]{Sound.ENTITY_VILLAGER_HURT}
            ),
//            new CommandItem("hug", new String[]{"cuddle"}, "Hug another player", new String[]{"hugged", "c", "villager.angry", "villager.hurt" }),
//            new CommandItem("kiss", new String[]{"smooch"}, "Kiss another player", new String[]{"kissed", "c", ""}),
//            new CommandItem("poke", new String[]{"prod"}, "Poke another player", new String[]{}),
//            new CommandItem("highfive", new String[]{"hf", "brofist"}, "High-five another player", new String[]{}),
//            new CommandItem("slap", new String[]{"hit"}, "Slap another player", new String[]{}),
//            new CommandItem("lick", new String[]{}, "Lick another player", new String[]{})
    };

    @Override
    public void onEnable() {
        // Register command executors
        for (CommandItem command : commands) {
            getCommand(command.getCommandData()[0]).setExecutor(new CustomCommandExecutor(command));
        }
    }

    private static class CommandItem {
        private final String[] commandData;
        private final String[] commandAliases;
        private final String[] commandDetails;
        private final String[] executionConfig;
        private final Particle[] executionParticle;
        private final Sound[] executionSound;

        public CommandItem(String[] commandData, String[] commandAliases, String[] commandDetails, String[] executionConfig, Particle[] executionParticle, Sound[] executionSound) {
            this.commandData = commandData;
            this.commandAliases = commandAliases;
            this.commandDetails = commandDetails;
            this.executionConfig = executionConfig;
            this.executionParticle = executionParticle;
            this.executionSound = executionSound;
        }

        public String[] getCommandData() {
            return commandData;
        }
        public String[] getAliasesData() {
            return commandAliases;
        }
        public String[] getCommandDetails() {
            return commandDetails;
        }
        public String[] getExecutionConfig() {
            return executionConfig;
        }
        public Particle[] executionParticle() {
            return executionParticle;
        }
        public Sound[] executionSound() {
            return executionSound;
        }
    }

    private static class CustomCommandExecutor implements CommandExecutor {
        private final CommandItem commandItem;

        public CustomCommandExecutor(CommandItem commandItem) {
            this.commandItem = commandItem;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            // Execute command logic here
            sender.sendMessage("Executing command: " + commandItem.getCommandData()[0]);
            return true;
        }
    }
}
