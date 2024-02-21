package arizonsoftware.axtonsemotes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
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
                    new Object[] { Particle.HEART, true, Sound.ENTITY_VILLAGER_CELEBRATE, true } // particle and sound
            ),
            new CommandItem(
                    new String[] { "kiss", "Kiss another player" }, // command name, description
                    new String[] { "smooch" }, // aliases
                    new Object[] { "kissed", "c", true }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.VILLAGER_ANGRY, true, Sound.ENTITY_VILLAGER_HURT, true } // particle and sound
            ),
            new CommandItem(
                    new String[] { "poke", "Poke another player" }, // command name, description
                    new String[] { "prod" }, // aliases
                    new Object[] { "poked", "e", true }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.SNOWBALL, true, Sound.ENTITY_CHICKEN_EGG, true } // particle and sound
            ),
            new CommandItem(
                    new String[] { "egg", "Throw an egg at another player" }, // command name, description
                    new String[] { "eggthrow" }, // aliases
                    new Object[] { "egged", "e", false }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.SNOWBALL, true, Sound.ENTITY_CHICKEN_EGG, true } // particle and sound
            ),
            new CommandItem(
                    new String[] { "highfive", "High-five another player" }, // command name, description
                    new String[] { "hf", "brofist" }, // aliases
                    new Object[] { "high-fived", "b", true }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.VILLAGER_HAPPY, true, Sound.ENTITY_VILLAGER_CELEBRATE, true } // particle
                                                                                                          // and sound
            ),
            new CommandItem(
                    new String[] { "slap", "Slap another player" }, // command name, description
                    new String[] { "hit" }, // aliases
                    new Object[] { "slapped", "c", true }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.VILLAGER_ANGRY, true, Sound.ENTITY_VILLAGER_HURT, true } // particle and
                                                                                                     // sound
            ),
            new CommandItem(
                    new String[] { "thank", "Thank another player" }, // command name, description
                    new String[] { "appreciate" }, // aliases
                    new Object[] { "thanked", "b", true }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.VILLAGER_HAPPY, true, Sound.ENTITY_VILLAGER_CELEBRATE, true } // particle
                                                                                                          // and sound
            ),
            new CommandItem(
                    new String[] { "lick", "Lick another player" }, // command name, description
                    new String[] {}, // aliases
                    new Object[] { "licked", "e", false }, // past tense verb, colour, self-executable?
                    new Object[] { Particle.FALLING_WATER, true, Sound.ENTITY_AXOLOTL_SPLASH, true } // particle and sound
            )
    };

    @Override
    public void onEnable() {
        int registeredCommands = 0;

        // Register command executors
        for (CommandItem command : commands) {
            String mainCommand = command.getCommandData()[0];
            PluginCommand pluginCommand = getCommand(mainCommand);

            if (pluginCommand != null) {
                pluginCommand.setExecutor(new CustomCommandExecutor(command));
                registeredCommands++;
            }

            // Register aliases
            String[] aliases = command.getAliasData();
            if (aliases != null && aliases.length > 0) {
                for (String alias : aliases) {
                    PluginCommand aliasCommand = getCommand(alias);
                    if (aliasCommand != null) {
                        aliasCommand.setExecutor(new CustomCommandExecutor(command));
                        registeredCommands++;
                    }
                }
            }
        }

        getLogger().info("Successfully registered " + registeredCommands + " commands!");
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
            try {

                // command data variables
                String cmdName = commandItem.getCommandData()[0];
                String cmdDesc = commandItem.getCommandData()[1];
                String cmdVerbTense = (String) commandItem.getCommandDetails()[0];
                String cmdTheme = (String) commandItem.getCommandDetails()[1];
                Particle execParticle = (Particle) commandItem.getExecutionConfig()[0];
                Sound execSound = (Sound) commandItem.getExecutionConfig()[2];

                if (!(sender instanceof Player)) {
                    // sender is not a player
                    sender.sendMessage("§cOnly players can execute this command!");
                    return true;
                }
                if (args.length == 0) {
                    // missing player argument
                    sender.sendMessage("§cUsage: /" + cmdName + " <player>");
                    return true;
                }

                // player variables
                Player p = (Player) sender;
                Player t = Bukkit.getServer().getPlayer(args[0]);

                if (t == null) {
                    // invalid player argument
                    sender.sendMessage("§cThat player is not online or does not exist!");
                    return true;
                }

                if (p.hasPermission("axtonsemotes." + cmdName)) {
                    Location pl = p.getLocation();
                    Location tl = t.getLocation();
                    if (p == t) {
                        if ((Boolean) commandItem.getCommandDetails()[2]) {
                            // self-execution is enabled, and the target and executor are the same
                            p.sendMessage(
                                    "§" + cmdTheme + "You " + cmdVerbTense + " §lyourself§r§" + cmdTheme + "!"); // to
                                                                                                                 // executing
                                                                                                                 // players
                            p.playSound(pl, execSound, 0.8F, 1.3F);
                            p.getLocation().getWorld().spawnParticle(execParticle, tl.add(0.5, 1.25, 0), 1);
                            String discordSRVCommmand = "discordsrv bcast **" + p.getName() + "** " + cmdVerbTense + " **" + t.getName() + "**";
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), discordSRVCommmand);
                        } else {
                            // self-execution is disabled!
                            p.sendMessage("§cYou cannot execute that command on yourself!");
                            return true;
                        }
                    } else {
                        p.sendMessage("§" + cmdTheme + "You " + cmdVerbTense + " §l"
                                + t.getName() + "§r§" + cmdTheme + "!"); // to executing player
                        if ((Boolean) commandItem.getExecutionConfig()[1])
                            p.playSound(pl, execSound, 1F, 1.3F);
                        if ((Boolean) commandItem.getExecutionConfig()[3])
                            p.getLocation().getWorld().spawnParticle(execParticle, tl.add(0.5, 1.25, 0),
                                    5);

                        t.sendMessage("§" + cmdTheme + "§l" + p.getName() + "§r §" + cmdTheme
                                + cmdVerbTense + " you!"); // send to target player
                        t.sendActionBar("§" + cmdTheme + cmdVerbTense.substring(0, 1).toUpperCase()
                                + cmdVerbTense.substring(1) + " by §l" + p.getName());
                        if ((Boolean) commandItem.getExecutionConfig()[1])
                            t.playSound(tl, execSound, 1F, 1.3F);
                        if ((Boolean) commandItem.getExecutionConfig()[3])
                            t.getLocation().getWorld().spawnParticle(execParticle, tl.add(0.5, 1.25, 0),
                                    5);
                    }
                } else {
                    p.sendMessage("§cYou don't have permission to use this command.");
                }
                return true;
            } catch (ArrayIndexOutOfBoundsException e) {
                // Handle the exception (e.g., log or notify the server owner)
                sender.sendMessage(
                        "§c[Axton's Emotes] An error occurred while processing the command.");
            }
            return true;
        }
    }
}
