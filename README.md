<p align="center">
    <img width="750" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/banner.png" alt="Axton's Emotes">
</p>

<p align="center">
    <a href="https://github.com/axtonprice/axtons-emotes/releases">
        <img src="https://img.shields.io/github/release/axtonprice/axtons-emotes?include_prereleases=&sort=semver&color=blue&label=latest&logo=GitHub" alt="Latest Release">
    </a>
    <a href="https://app.codacy.com/gh/axtonprice/axtons-emotes/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade">
        <img src="https://app.codacy.com/project/badge/Grade/09d0abf7deae4a0ea1c5b871eab45464" alt="Codacy Grade">
    </a>
    <a href="https://discord.gg/dP3MuBATGc">
        <img src="https://img.shields.io/discord/308323056592486420?logo=discord&logoColor=white" alt="Discord Chat">
    </a>
    <br>
    <a href="https://modrinth.com/plugin/axtonsemotes">
        <img src="https://img.shields.io/modrinth/dt/axtonsemotes?logo=Modrinth&label=Modrinth" alt="Modrinth Downloads">
    </a>
    <a href="https://www.curseforge.com/minecraft/bukkit-plugins/axtons-emotes">
        <img src="https://img.shields.io/curseforge/dt/1086817?logo=CurseForge&label=CurseForge" alt="CurseForge Downloads">
    </a>
    <a href="https://www.spigotmc.org/resources/axtons-emotes.119499/">
        <img src="https://img.shields.io/spiget/downloads/119499?logo=SpigotMC&label=Spigot" alt="Spigot Version">
    </a>
        <a href="https://builtbybit.com/resources/axtons-emotes.84984/">
        <img src="https://img.shields.io/badge/BuiltByBit-0?logo=builtbybit&labelColor=white&color=2D87C3" alt="BuiltByBit">
    </a>
</p>

# Axton's Emotes
> A light-weight, configurable chat emotes plugin for 1.20+ Spigot/Paper servers.

Available in 10+ languages, including Spanish, French, Italian, German.. See the full list [here](https://github.com/axtonprice/axtons-emotes/wiki/Languages).
<p align="center">
    <img width="250" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/promo1.png" alt="Promo Image 1">
    <img width="250" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/promo2.png" alt="Promo Image 2">
</p>

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/installation_text.png" alt="Installation">

### Installing the plugin

1. **Download**: Get the latest `.jar` from the [Releases](https://github.com/axtonprice/axtons-emotes/releases) tab, or from [Modrinth](https://modrinth.com/plugin/axtonsemotes).
2. **Deploy**: Place the `.jar` in your server's `plugins/` folder.
3. **Restart**: Restart your server. PaperMC is recommended for optimal performance.
4. **Configure**: Modify `config.yml` to your liking, then reload the plugin with `/ae reload`.
5. **Verify**: Check you are running the latest version by using `/ae version`.

> **Note:** Java 17 or higher is required to run this plugin. Please refer to [the wiki](https://github.com/axtonprice/axtons-emotes/wiki/installation) for more details on installation requirements.

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/screenshots_text.png" alt="Screenshots">

### Ingame Examples

<p align="center">
    <img width="300" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/cmd_example1.gif" alt="Command example">
    <img width="300" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/cmd_example2.gif" alt="Command example">
    <img width="300" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/cmd_example3.gif" alt="Command example">
</p>

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/features_text.png" alt="Features">

### Emote Types

#### ✨ Shared Emotes  
Interact with other players with targeted emotes that involve another user.

#### 😊 Expression Emotes  
Express yourself to the entire server - these emotes are broadcast to everyone and do not target a player.

---

### 🧩 Examples of built-in emotes
| Shared Emotes              | Expression Emotes |
|----------------------------|-------------------|
| `/e hug <player>`          | `/e cry`          |
| `/e poke <player>`         | `/e laugh`        |
| `/e highfive <player>`     | `/e wave`         |
| `/e kiss <player>`         | `/e gasp`         |
| `/e slap <player>`         | `/e shrug`        |
| `/e stare <player>`        | `/e facepalm`     |
| `/e thank <player>`        | `/e cheer`        |
| `/e applaud <player>`      | `/e bow`          |

---

Permissions for all emotes are granted by default unless an emote is disabled or the user is explicitly denied permission to all emotes with `axtonsemotes.emotes.use`.

<br>
<img width="540" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/commands_perms_text.png" alt="Commands & Features">

### Commands & Permissions

List of commands and associated permission nodes.

> Please refer to [the wiki](https://github.com/axtonprice/axtons-emotes/wiki/Commands) for more details on commands & permission nodes.

| Command                             | Description                                                | Permission Node                       |
|-------------------------------------|------------------------------------------------------------|---------------------------------------|
| `/emote`, `/e`                          | Execute an emote command                   | `axtonsemotes.emotes.use` *Granted by default* |
| `/emotes`                           | List all emotes available to the player.                   | `axtonsemotes.emotes.list` *Granted by default* |
| `/toggleemotes`                           | Toggles shared emotes individually, disallowing shared emote commands to be used on or by that player.                   | `axtonsemotes.emotes.selftoggle` *Granted by default* |
| `/ae reload`                        | Reload the plugin configuration.                           | `axtonsemotes.admin.version`<br>Default: op  |
| `/ae version`                       | Check the plugin's current version and the latest release. | `axtonsemotes.admin.reload`<br>Default: op |
| `/ae toggledebug`                   | Toggles debug mode and will show detailed logs of command plugin executions. | `axtonsemotes.admin.toggledebug`<br>Default: op |
| `/ae togglemetrics`                   | Toggles BStats anonymous metrics tracking. | `axtonsemotes.admin.togglemetrics`<br>Default: op |
| `/ae resetlang`                   | Resets language configuration files to the plugin defaults. | `axtonsemotes.admin.resetlang`<br>Default: op |
| `/ae resetemotes`                   | Resets emote.yml configuration to the plugin defaults. | `axtonsemotes.admin.resetemotes`<br>Default: op |
| `/ae help`                   | Provides link to the plugin documentation. | `axtonsemotes.admin.help`<br>Default: op |
|                                     | Notify administrator of plugin updates when they join.            | `axtonsemotes.admin.updatenotify`<br>Default: op |
|                                     | Allows players to use `/emotes`, even if the feature is disabled in the config.            | `axtonsemotes.admin.list-override`<br>Default: op  |
|                                     | Allows players to bypass the minimum block distance configured for shared emotes.            | `axtonsemotes.admin.bypassradius`<br>Default: op  |
|                                     | Allows players to bypass the configured cooldown time for emote executions.            | `axtonsemotes.admin.bypasscooldown`<br>Default: op  |

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/configuration_text.png" alt="Configuration">

### Configuration

To configure the plugin, edit the `config.yml` file in the `AxtonsEmotes` plugin folder.

After updating the configuration, reload it with `/ae reload` (requires the `axtonsemotes.admin.reload` permission).

> Please refer to [the wiki](https://github.com/axtonprice/axtons-emotes/wiki/Configuration) for more details on the plugin configuration files.

```yml
# >> Language
# Sets the language used for in-game plugin messages.
# For a full list of available languages, please refer to https://github.com/axtonprice/axtons-emotes/wiki/Languages
language: en

# >> Self-Executions
# Allows players to execute emote commands on themselves.
# Default: true
allow-self-executions: false

# >> List Commands
# Enables the /emotes and /expressions commands to view permitted emotes. When enabled, anyone can list commands.
# When disabled, only server operators or players with the 'axtonsemotes.admin.list-override' permission can list commands.
# Default: true
allow-list-commands: true

# >> Shared Emotes Toggle
# Allows players to use /toggleemotes, removing the ability for others to execute shared emotes on them.
# If both below are enabled, the player must have the 'axtonsemotes.emotes.selftoggle' permission to run the toggle command.
allow-self-toggle:
  # Default: true
  enabled: true
  # Default: false
  require-permission: false

# >> Shared Emote Radius
# Sets the minimum distance (in blocks) required between the sender and target player to execute shared emotes.
# Players with axtonsemotes.admin.bypassradius permission can bypass the configured radius limit below.
emote-radius:
  # Default: true
  enabled: true
  # Default: 5
  distance: 5

# >> Emote Cooldown
# Sets a cooldown period (in seconds) between emote executions per player.
# Players with axtonsemotes.admin.bypasscooldown permission can bypass the configured cooldown below.
emote-cooldown:
  # Default: true
  enabled: true
  # Default: 3 (seconds)
  duration-seconds: 3

# >> Default Effects
# Specifies the default particle and sound effects if the specified enum constant is invalid (NOT if the effect is set to 'none').
# Defaults: CRIT, ENTITY_ITEM_PICKUP
default-effects:
  particle: "CRIT"
  sound: "ENTITY_ITEM_PICKUP"

# Configure debug mode, which logs detailed information about command executions. This is useful
# for troubleshooting but will result in many console messages.
debug-mode:
  # Toggles additional console information during startup and command executions.
  # Default: false
  enabled: false

  # Specify whether to log debug messages to a file. Ignored if 'enabled' above is set to false.
  # Default: true
  log-to-file: true

  # Specifies the name of the file which debug logs are saved to. Set to none to disable logging to a file.
  # Default: debug.log
  file-name: "debug.log"

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
enable-metrics: true
```

### Emote Configuration
Below is an example of how to configure an expression and a shared emote command. Please refer to the [wiki](https://github.com/axtonprice/axtons-emotes/wiki/Configuration) for more information on how to configure emotes.
```yml
commands:
  cry:
    enabled: true
    type: "expression"
    messages:
      player: "&7You cry yourself in sorrow.." # Sent to the sender player
      target: "&7&l%player%&r&7 cries in a corner.." # Sent to all players
    effects:
      particle: "CRIT" # Only played to the sender player
      sound: "ENTITY_CAT_HURT" # Only played to the sender player

  hug:
    enabled: true
    type: "shared"
    messages:
      player: "&cYou hugged &l%target%&r&c!" # Sent to the sender player
      target: "&c&l%player%&r&c hugged you!" # Sent to the targeted player
    effects:
      particle: "HEART" # Played to both players
      sound: "ENTITY_CAT_PURREOW" # Played to both players
```

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/credits_text.png">

### axtonprice ✨

[![Sponsor](https://img.shields.io/badge/sponsor-gray?logo=GitHub-Sponsors&logoColor=#EA4AAA)](https://github.com/sponsors/axtonprice) 
[![Patreon](https://img.shields.io/badge/Patreon-ee6352?logo=patreon&logoColor=black)](https://www.patreon.com/axtonprice/) 
[![Buy Me A Coffee](https://img.shields.io/badge/Buy%20Me%20A%20Coffee-FFDD00?logo=buymeacoffee&logoColor=black)](https://buymeacoffee.com/axtonprice)

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/refs/heads/main/.github/media/license_text.png">

### GPLv3 License

This project is licensed under the GNU General Public License version 3. View the project license [here](https://github.com/axtonprice/axtons-emotes/blob/main/LICENSE).