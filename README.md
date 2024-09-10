<p align="center">
    <img width="650" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/banner.png" alt="Axton's Emotes">
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
    <a href="https://modrinth.com/plugin/axtonsemotes">
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
> A lightweight social emotes plugin for Bukkit/Spigot/Paper 1.21.

Available in English, Spanish, French, Italian, and Russian.

<p align="center">
    <img width="300" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/promo1.png" alt="Promo Image 1">
    <img width="300" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/promo2.png" alt="Promo Image 2">
</p>

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/installation_text.png" alt="Installation">
<br>
<br>

1. **Download**: Get the latest `.jar` from the [Releases](https://github.com/axtonprice/axtons-emotes/releases) tab.
2. **Deploy**: Place the `.jar` in your server's `/plugins` folder.
3. **Restart**: Restart your server. PaperMC is recommended for optimal performance.
4. **Configure**: Modify the `config.yml` to your liking, then reload the plugin with `/ae reload`.

> **Note:** Java 21 or higher is required to run this plugin.

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/features_text.png" alt="Features">
<br>

### Emotes

Interact with other players using these emotes. Permissions for these commands are granted by default unless the command is disabled.

| Command                                 | Description                                 |
|-----------------------------------------|---------------------------------------------|
| `/dapup <player>`<br>`/dap <player>`    | Dap-up another player in a friendly manner. |
| `/highfive <player>`<br>`/hf <player>`  | High-five another player energetically.     |
| `/hug <player>`<br>`/cuddle <player>`   | Hug another player warmly.                  |
| `/kiss <player>`<br>`/smooch <player>`  | Kiss another player affectionately.         |
| `/poke <player>`                        | Poke another player playfully.              |
| `/stomp <player>`                       | Stomp on another player's foot forcefully.  |
| `/slap <player>`                        | Slap another player sharply.                |
| `/stab <player>`<br>`/shank <player>`   | Stab another player aggressively.           |
| `/stare <player>`                       | Stare at another player intently.           |
| `/thank <player>`<br>`/ty <player>`     | Thank another player sincerely.             |
| `/applaud <player>`<br>`/clap <player>` | Applaud another player enthusiastically.    |

### Expressions

Express yourself to all players on the server. Permissions for these commands are granted by default unless the command is disabled.

| Command           | Description                             |
|-------------------|-----------------------------------------|
| `/wave`           | Wave hello to everyone cheerfully.      |
| `/cry`<br>`/sulk` | Express your sadness to everyone openly.|

### Jumpscares

Scare yourself in unique ways. Permissions for these commands are granted by default unless the command is disabled. Soon™ players will be able to jumpscare each other (toggleable).

| Command                         | Description                                             |
|---------------------------------|---------------------------------------------------------|
| `/bogscare`<br>`/bog`           | Perform a jumpscare on yourself with a bog effect.      |
| `/guardianscare`<br>`/guardian` | Perform a jumpscare on yourself with a guardian effect. |

### Miscellaneous

Additional commands and features:

| Command                             | Description                                                | Permission Node                       |
|-------------------------------------|------------------------------------------------------------|---------------------------------------|
| `/emotes`<br>`/ae emotes`           | List all emotes available to the player.                   | *Granted by default, unless disabled* |
| `/expressions`<br>`/ae expressions` | List all expressions available to the player.              | *Granted by default, unless disabled* |
| `/jumpscares`<br>`/ae jumpscares`   | List all jumpscares available to the player.               | *Granted by default, unless disabled* |
| `/ae reload`                        | Reload the plugin configuration.                           | `axtonsemotes.reload`<br>Default: op  |
| `/ae version`                       | Check the plugin's current version and the latest release. | `axtonsemotes.version`<br>Default: op |
|                                     | Notify administrator of plugin updates on join.            | `axtonsemotes.updatenotify`<br>Default: op            |

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/configuration_text.png" alt="Configuration">
<br><br>

To customize the plugin, edit the `config.yml` file:

```yml
# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
# Global Configuration
# Configure global plugin settings and features:
# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

# Choose the language for the plugin. To create a custom language, copy and modify the 'en.yml'
# file. Supported languages include: 'en' (English), 'fr' (Français/French), 'es' (Español/Spanish), 
# 'it' (Italiano/Italian), 'ru' (Русский/Russian).
language: en

# Allows players to use shared emote commands on themselves.
allow-self-executions: false

# Allows players to view a list of all emotes, expressions, and jumpscares available, based on 
# their permissions.
allow-list-commands: true

# Configure debug mode, which logs detailed information about command executions. This is useful 
# for troubleshooting but will result in many console messages.
debug-mode:
  # Enable or disable debug mode.
  enabled: false
  # Specify whether to log debug messages to a file. Ignored if 'enabled' is false.
  log-to-file: true
  # Specify the file name for the debug log. Ignored if 'enabled' is false.
  log-file: 'debug.log'

# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
# Plugin Versioning
# Configure update checks and notifications:
# >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

# Enables automatic checking for new plugin updates.
check-for-latest: true

# Notifies players with the 'axtonsemotes.updatenotify' permission when a new update is available.
notify-on-update: true
```
After updating the configuration, reload it with `/ae reload`.

Default configurations for the emote commands are available on the GitHub. For more information, read the [Wiki](https://github.com/axtonprice/axtons-emotes/wiki).

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/credits_text.png">

## Main Author

### axtonprice ✨

[![GitHub](https://img.shields.io/badge/Github-%23121011.svg?logo=github&logoColor=white)](https://github.com/axtonprice) |  [![Discord](https://img.shields.io/discord/308323056592486420?logo=discord&%22%20alt=%22Chat%20on%20Discord%22)](https://discord.gg/dP3MuBATGc)
| [![Github Sponsors](https://img.shields.io/badge/sponsor-30363D?logo=GitHub-Sponsors&logoColor=#EA4AAA)](https://github.com/sponsors/axtonprice) | [![Buy Me a Coffee](https://img.shields.io/badge/-buy_me_a%C2%A0coffee-gray?logo=buy-me-a-coffee)](https://buymeacoffee.com/axtonprice)
)

Donate:

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://buymeacoffee.com/axtonprice)

<br>
<img width="350" src="https://raw.githubusercontent.com/axtonprice/axtons-emotes/main/.github/media/license_text.png">
<br>
<br>

This project is MIT licensed. View the license [here](https://github.com/axtonprice/axtons-emotes/blob/main/LICENSE).

MIT © axtonprice