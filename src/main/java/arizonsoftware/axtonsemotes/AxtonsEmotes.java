package arizonsoftware.axtonsemotes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AxtonsEmotes extends JavaPlugin {

    // Command items array
    private final CommandItem[] commands = {
            /*
             * color codes:
             * anger,love = c
             * happy,excitement = b
             * misc = e
             */
            new CommandItem(
                    new String[] { "hug", "Hug another player" }, // command name, description
                    new String[] { "cuddle" }, // aliases
                    new Object[] { "hugged", "c", true }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.HEART, Sound.ENTITY_VILLAGER_CELEBRATE } // particle and sound
            ),
            new CommandItem(
                    new String[] { "kiss", "Kiss another player" }, // command name, description
                    new String[] { "smooch" }, // aliases
                    new Object[] { "kissed", "c", true }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.VILLAGER_ANGRY, Sound.ENTITY_VILLAGER_HURT } // particle and sound
            ),
            new CommandItem(
                    new String[] { "poke", "Poke another player" }, // command name, description
                    new String[] { "prod" }, // aliases
                    new Object[] { "poked", "e", false }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.VILLAGER_ANGRY, Sound.ENTITY_CHICKEN_EGG } // particle and sound
            ),
            new CommandItem(
                    new String[] { "highfive", "High-five another player" }, // command name, description
                    new String[] { "hf", "brofist" }, // aliases
                    new Object[] { "high-fived", "b", true }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.VILLAGER_HAPPY, Sound.ENTITY_VILLAGER_CELEBRATE } // particle and sound
            ),
            new CommandItem(
                    new String[] { "slap", "Slap another player" }, // command name, description
                    new String[] { "hit" }, // aliases
                    new Object[] { "slapped", "c", true }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.VILLAGER_ANGRY, Sound.ENTITY_VILLAGER_HURT } // particle and sound
            ),
            new CommandItem(
                    new String[] { "lick", "Lick another player" }, // command name, description
                    new String[] {}, // aliases
                    new Object[] { "licked", "e", false }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.GLOW_SQUID_INK, Sound.ENTITY_FROG_TONGUE } // particle and sound
            )
    };

    @Override
    public void onEnable() {
        // Register command executors
        for (CommandItem command : commands) {
            getCommand(command.getCommandData()[0]).setExecutor(new CustomCommandExecutor(command));
        }
        getLogger().info("Successfully registered commands!");
    }

    private static class CommandItem {

        private final String[] commandData;
        private final String[] commandAliases;
        private final Object[] commandDetails;
        private final Object[] executionConfig;

        public CommandItem(String[] commandData, String[] commandAliases, Object[] commandDetails,
                Object[] executionConfig) {
            this.commandData = commandData;
            this.commandAliases = commandAliases;
            this.commandDetails = commandDetails;
            this.executionConfig = executionConfig;
        }

        public String[] getCommandData() {
            return commandData;
        }

        public String[] getAliasData() {
            return commandAliases;
        }

        public Object[] getCommandDetails() {
            return commandDetails;
        }

        public Object[] getExecutionConfig() {
            return executionConfig;
        }
    }

    private static class CustomCommandExecutor implements CommandExecutor {
        private final CommandItem commandItem;

        public CustomCommandExecutor(CommandItem commandItem) {
            this.commandItem = commandItem;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            // player vars
            Player p = (Player) sender;
            Player t = Bukkit.getServer().getPlayer(args[0]);
            // command data vars
            String cmdName = commandItem.getCommandData()[0];
            String cmdDesc = commandItem.getCommandData()[1];
            String cmdVerbTense = (String) commandItem.getCommandDetails()[0];
            String cmdTheme = (String) commandItem.getCommandDetails()[1];
            Particle execParticle = (Particle) commandItem.getExecutionConfig()[0];
            Sound execSound = (Sound) commandItem.getExecutionConfig()[1];

            if (!(sender instanceof Player)) {
                // sender is not a player
                sender.sendMessage("§cOnly players can execute this command!");
                return true;
            }
            if (args.length == 0) {
                // missing player argument
                p.sendMessage("§c" + cmdDesc + ". Usage: /" + cmdName + " <player>");
                return true;
            }
            if (t == null) {
                // invalid player argument
                p.sendMessage("§cThat player is not online!");
                return true;
            }
            try {
                if (args.length == 1) {
                    if (p.hasPermission("axtonsemotes." + cmdName)) {
                        Location pl = p.getLocation();
                        Location tl = t.getLocation();

                        if (p == t) {
                            if ((Boolean) commandItem.getCommandDetails()[2] == true) {
                                // self-execution is enabled, and the target and executor are the same
                                p.sendMessage("§" + cmdTheme + "You " + cmdVerbTense + " yourself!"); // to executing player
                                p.playSound(pl, execSound, 0.8F, 1.3F);
                                p.getLocation().getWorld().spawnParticle(execParticle, tl.add(0, 1, 0), 1);
                            } else {
                                // self-execution is disable!
                                p.sendMessage("§cYou cannot execute that command on yourself!");
                                return true;
                            }
                        } else {
                            p.sendMessage("§" + cmdTheme + "You " + cmdVerbTense + " §l"
                                    + t.getName() + "§r§" + cmdTheme + "!"); // to executing player
                            p.playSound(pl, execSound, 0.8F, 1.3F);
                            p.getLocation().getWorld().spawnParticle(execParticle, tl.add(0, 1, 0),
                                    1);

                            t.sendMessage("§" + cmdTheme + "§l" + p.getName() + "§r §" + cmdTheme
                                    + cmdVerbTense + " you!"); // send to target player
                            t.sendActionBar("§" + cmdTheme + cmdVerbTense.substring(0, 1).toUpperCase()
                                    + cmdVerbTense.substring(1) + " by " + p.getName() + "!");
                            t.playSound(tl, execSound, 0.8F, 1.3F);
                            t.getLocation().getWorld().spawnParticle(execParticle, tl.add(0, 1, 0),
                                    1);
                        }

                    } else {
                        p.sendMessage("§cYou don't have permission to use this command.");
                    }
                    return true;
                }
            } catch (

            ArrayIndexOutOfBoundsException e) {
                // Handle the exception (e.g., log or notify the server owner)
                sender.sendMessage(
                        "§c[Axton's Emotes] An error occurred while processing the command.");
            }

            return true;

        }
    }
}
