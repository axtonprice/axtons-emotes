<p align="center">
    <img width="750" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/banner.png" alt="Axton's Emotes">
</p>

<p align="center">
    <a href="https://github.com/axtonprice/axtons-emotes/releases">
        <img src="https://img.shields.io/github/release/axtonprice/axtons-emotes?include_prereleases=&sort=semver&color=blue&label=latest&logo=GitHub" alt="Latest Release">
    </a>
    <a href="https://app.codacy.com/gh/axtonprice/axtons-emotes/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade">
        <img src="https://app.codacy.com/project/badge/Grade/09d0abf7deae4a0ea1c5b871eab45464" alt="Codacy Grade">
    </a>
    <a href="https://modrinth.com/plugin/axtonsemotes">
        <img src="https://img.shields.io/modrinth/dt/axtonsemotes?logo=Modrinth&label=Modrinth" alt="Modrinth Downloads">
    </a>
    <a href="https://www.curseforge.com/minecraft/bukkit-plugins/axtons-emotes">
        <img src="https://img.shields.io/curseforge/dt/1086817?logo=CurseForge&label=CurseForge" alt="CurseForge Downloads">
    </a>
    <a href="https://www.spigotmc.org/resources/axtons-emotes.119499/">
        <img alt="Spigot Version" src="https://img.shields.io/spiget/downloads/119499?logo=SpigotMC&label=Spigot">
    </a>
    <a href="https://discord.gg/dP3MuBATGc">
        <img src="https://img.shields.io/discord/308323056592486420?logo=discord&logoColor=white" alt="Discord Chat">
    </a>
</p>

# Axton's Emotes
> A lightweight social emotes plugin for Spigot/Paper/Purpur 1.20+.

Available in English, Spanish, French, Italian, and Russian.

<p align="center">
    <img width="300" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/promo1.png" alt="Promo Image 1">
    <img width="300" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/promo2.png" alt="Promo Image 2">
</p>

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/installation_text.png" alt="Installation">
<br>

1. **Download**: Get the latest `.jar` from the [Releases](https://github.com/axtonprice/axtons-emotes/releases) tab, or from [Modrinth](https://modrinth.com/plugin/axtonsemotes).
2. **Deploy**: Place the `.jar` in your server's `/plugins` folder.
3. **Restart**: Restart your server. PaperMC is recommended for optimal performance.
4. **Configure**: Modify the `config.yml` to your liking, then reload the plugin with `/ae reload`.

> **Note:** Java 17 or higher is required to run this plugin.

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/features_text.png" alt="Features">
<br>

**Shared emotes** - Interact with other players using these emotes. 

**Expressions** - Expression emotes are broadcast to all players on the server and do not target another player.

Examples of some pre-configured emotes:
| Shared Emotes           | Expression Emotes                             |
|-------------------|-----------------------------------------|
| `/e highfive <player>` | `/e wave` |
| `/e hug <player>`      | `/e cry` |
| `/e kiss <player>`     | `/e gasp` |
| `/e poke <player>`     | `/e laugh` |
| `/e slap <player>`     | `/e shrug` |
| `/e stare <player>`    | `/e facepalm` |
| `/e thank <player>`    | `/e cheer` |
| `/e applaud <player>`  | `/e bow` |

Permissions for all emotes are granted by default unless the emote is disabled or the user is explicitly denied permission to `axtonsemotes.emotes.use`.

### Commands & Permissions

List of commands and associated permission nodes.

| Command                             | Description                                                | Permission Node                       |
|-------------------------------------|------------------------------------------------------------|---------------------------------------|
| `/emote`, `/e`                           | Execute an emote command                   | `axtonsemotes.emotes.use` *Granted by default, unless emote is disabled* |
| `/emotes`                           | List all emotes available to the player.                   | `axtonsemotes.emotes.list` *Granted by default, unless disabled in config* |
| `/toggleemotes`                           | Toggles shared emotes individually, disallowing shared emote commands to be used on or by that player.                   | `axtonsemotes.emotes.selftoggle` *Granted by default, unless configured otherwise in config.yml* |
| `/ae reload`                        | Reload the plugin configuration.                           | `axtonsemotes.admin.version`<br>Default: op  |
| `/ae version`                       | Check the plugin's current version and the latest release. | `axtonsemotes.admin.reload`<br>Default: op |
| `/ae toggledebug`                   | Toggles debug mode and will show detailed logs of command plugin executions. | `axtonsemotes.admin.toggledebug`<br>Default: op |
| `/ae togglemetrics`                   | Toggles BStats anonymous metrics tracking. | `axtonsemotes.admin.togglemetrics`<br>Default: op |
| `/ae resetlang`                   | Resets language configuration files to the plugin defaults. | `axtonsemotes.admin.resetlang`<br>Default: op |
| `/ae help`                   | Provides link to the plugin documentation. | `axtonsemotes.admin.help`<br>Default: op |
|                                     | Notify administrator of plugin updates when they join.            | `axtonsemotes.admin.updatenotify`<br>Default: op |
|                                     | Allows players to list emotes with `/emotes` even when the feature is disabled in config.yml            | `axtonsemotes.admin.list-override`<br>Default: op  |

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/configuration_text.png" alt="Configuration">
<br>

To customize the plugin, edit the `config.yml` file:

```yml
# >> Language
# Sets the language used for in-game messages.
# Options: en (English), es (Spanish), fr (French), it (Italian), ru (Russian/Русский)
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
# Default: true
allow-self-toggle:
  enabled: true
  require-permission: false

# >> Default Effects
# Specifies the default particle and sound effects if the specified ENUM is invalid (NOT if the effect is set to 'none').
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
After updating the configuration, reload it with `/ae reload`. Requires the `axtonsemotes.admin.reload` permission.

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/credits_text.png">

## Main Author


### axtonprice ✨

[![GitHub](https://img.shields.io/badge/Github-%23121011.svg?logo=github&logoColor=white)](https://github.com/axtonprice) |  [![Discord](https://img.shields.io/discord/308323056592486420?logo=discord&%22%20alt=%22Chat%20on%20Discord%22)](https://discord.gg/dP3MuBATGc)
| [![Github Sponsors](https://img.shields.io/badge/sponsor-30363D?logo=GitHub-Sponsors&logoColor=#EA4AAA)](https://github.com/sponsors/axtonprice) | [![Buy Me a Coffee](https://img.shields.io/badge/-buy_me_a%C2%A0coffee-gray?logo=buy-me-a-coffee)](https://buymeacoffee.com/axtonprice)

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://buymeacoffee.com/axtonprice)

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/license_text.png">
<br>

This project is MIT licensed. View the license [here](https://github.com/axtonprice/axtons-emotes/blob/main/LICENSE).

MIT © axtonprice