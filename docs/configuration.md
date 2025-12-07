# Plugin Configuration
See below the default configuration values for the plugin.

```yml
# >> Language
# Sets the language used for in-game plugin messages.
# For a full list of available languages, please refer to https://github.com/axtonprice/axtons-emotes/wiki/Languages
language: en

# >> Self-Executions
# Allows players to execute emote commands on themselves.
# Default: true
allow-self-executions: true

# >> List Commands
# Enables the /emotes and /expressions commands to view permitted emotes. When enabled, anyone can list commands.
# When disabled, only server operators or players with the 'axtonsemotes.admin.list-override' permission can list commands.
# Default: true
allow-list-commands: true

# >> Shared Emotes Toggle
# Allows players to use /toggleemotes, removing the ability for others to execute shared emotes on them.
# If both below are enabled, the player must have the 'axtonsemotes.emotes.selftoggle' permission to run the toggle command.
# Default: true
allow-self-toggle:
  enabled: true
  require-permission: false

# >> Default Effects
# Specifies the default particle and sound effects if the specified enum constant is invalid (NOT if the effect is set to 'none').
# Defaults: CRIT, ENTITY_ITEM_PICKUP
default-effects:
  particle: CRIT
  sound: ENTITY_ITEM_PICKUP

# Configure debug mode, which logs detailed information about command executions. This is useful
# for troubleshooting but will result in many console messages.
debug-mode:
  # Toggles additional console information during startup and command executions.
  # Default: false
  enabled: true
  
  # Specify whether to log debug messages to a file. Ignored if 'enabled' above is set to false.
  # Default: true
  log-to-file: true
  
  # Specifies the name of the file which debug logs are saved to. Set to none to disable logging to a file.
  # Default: debug.log
  file-name: debug.log

# >> Plugin Versioning & Updates
# Configure update checks and notifications:
updates:
  # Automatically checks for plugin updates every 12 hours and on server startup.
  # Default: true
  check-for-latest: true
  
  # Notifies players with the axtonsemotes.admin.updatenotify permission when a newer version is available.
  # Default: true
  notify-players-on-update: true

# >> Metrics Tracking
# Enables BStats anonymous metrics tracking. Learn more at https://bstats.org/
# Default: true
enable-metrics: false
```

> ⚠️ **Warning:** Do not modify the `config-version` property in your config.yml! Doing so will cause the plugin to reset your configuration file to the defaults, purging any changes you have made.

## Emote Configuration

Below is an example of a correctly structured emote command.

```yml
commands:
  cry:
    enabled: true
    type: "expression"
    messages:
      player: "&7You cry yourself in sorrow..."
      target: "&7&l%player%&r&7 cries in a corner..."
    effects:
      particle: "NONE"
      sound: "ENTITY_CAT_HURT"
```

**Explanation:**

* `enabled`: `true` or `false` – whether the emote is active.
* `type`: `expression`, or `shared`. Expression emotes are displayed to the whole server, shared emotes are between two players.
* `messages.player`: Message shown to the player executing the emote.
* `messages.target`: Message shown to targeted player, or all players if it's an expression emote.
* `effects.particle`: Particle effect to play (use a valid `Particle` enum) for both players (only played to the sender if it's an expression emote)
* `effects.sound`: Sound effect to play (use a valid `Sound` enum) for both players (only played to the sender if it's an expression emote)

---

## Sound & Particle Effects

Both `particle` and `sound` fields must correspond to valid Bukkit **enum constants**:

* **Particle examples:** `CAMPFIRE_SIGNAL_SMOKE`, `COPPER_FIRE_FLAME`, `HEART`, `NONE`
* **Sound examples:** `BLOCK_ANCIENT_DEBRIS_FALL`, `BLOCK_ANVIL_BREAK`, `ENTITY_CAT_PURR`, `ENTITY_CAT_HURT`

Invalid particle/sound enums will default to the specified values in `config.yml`:

```yml
default-effects:
  particle: "CRIT"
  sound: "ENTITY_ITEM_PICKUP"
```

> **Important:** Valid enums will depend on your Minecraft server version. Some particle/sound enums may not exist in older versions (and vice versa). Some particle/sounds may also require additional parameters, which is not currently supported.

* [Particle effects (SpigotMC Javadocs)](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html#enum-constant-summary)
* [Sound effects (SpigotMC Javadocs)](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html#enum-constant-summary)
