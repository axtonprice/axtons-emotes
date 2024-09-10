<p align="center">
    <img width="650" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/title.png">
</p>
<p align="center">
    <a href="https://github.com/axtonprice/axtons-emotes/releases">
      <img src="https://img.shields.io/github/release/axtonprice/axtons-emotes?include_prereleases=&sort=semver&color=blue&label=latest">
    </a>
    <a href="https://app.codacy.com/gh/axtonprice/axtons-emotes/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade">
      <img src="https://app.codacy.com/project/badge/Grade/09d0abf7deae4a0ea1c5b871eab45464">
    </a>
    <a href="https://modrinth.com/plugin/axtonsemotes">
      <img src="https://img.shields.io/modrinth/dt/axtonsemotes?logo=Modrinth">
    </a>
    <a href="https://modrinth.com/plugin/axtonsemotes">
      <img src="https://img.shields.io/curseforge/dt/1086817?logo=CurseForge">
    </a>
    <a href="https://discord.gg/dP3MuBATGc">
      <img src="https://img.shields.io/discord/308323056592486420?logo=discord&logoColor=white%22%20alt=%22Chat%20on%20Discord%22">
    </a>
</p>

# Axton's Emotes
> A lightweight social emotes plugin for Bukkit/Spigot/Paper 1.21

<p align="center">
  <img width="300" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/image1.png">
  <img width="300" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/image2.png">
</p>

<br>
<img width="350" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/installation.png">
<hr>

1. **Download**: Grab the latest `.jar` file from the [releases tab](https://github.com/axtonprice/axtons-emotes/releases).
2. **Deploy**: Place the downloaded file into your server's `/plugins` folder.
3. **Restart**: Restart your server. For optimal performance, using PaperMC is recommended.

> A minimum of Java 21 is required on the server to run this plugin.

<br>
<img width="230" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/features.png">
<hr>

### Emotes

Interact with other players using these emotes:

| Command              | Description                    | Permission Node            |
|----------------------|--------------------------------|----------------------------|
| `/dapup <player>`<br>`/dap <player>`    | Dap-up another player.         | `axtonsemotes.dapup`       |
| `/highfive <player>`<br>`/hf <player>` | High-five another player.      | `axtonsemotes.highfive`    |
| `/hug <player>`      | Hug another player.            | `axtonsemotes.hug`         |
| `/kiss <player>`<br>`/smooch <player>`     | Kiss another player.           | `axtonsemotes.kiss`        |
| `/poke <player>`     | Poke another player.           | `axtonsemotes.poke`        |
| `/stomp <player>`    | Stomp on another player's foot.   | `axtonsemotes.stomp`       |
| `/slap <player>`     | Slap another player.           | `axtonsemotes.slap`        |
| `/stab <player>`<br>`/shank <player>`     | Stab another player.           | `axtonsemotes.stab`        |
| `/stare <player>`    | Stare at another player.       | `axtonsemotes.stare`       |
| `/thank <player>`<br>`/ty <player>`   | Thank another player.          | `axtonsemotes.thank`       |
| `/applaud <player>`<br>`/clap <player>`  | Applaud another player.        | `axtonsemotes.applaud`     |

### Expressions

Express yourself to all players on the server:

| Command   | Description                     | Permission Node            |
|-----------|---------------------------------|----------------------------|
| `/wave`   | Wave at all players.            | `axtonsemotes.wave`        |
| `/cry`<br>`/sulk`    | Cry in front of all players.    | `axtonsemotes.cry`         |

### Miscellaneous

Miscellaneous commands & extra features:

| Command           | Description                      | Permission Node            |
|-------------------|----------------------------------|----------------------------|
| `/bog`<br>`/bogged`         | Jumpscare yourself!       | `axtonsemotes.bog`        |
| `/emotes`<br>`/ae emotes`         | List all available emotes.       | `axtonsemotes.emotes`        |
| `/expressions`<br>`/ae expressions`    | List all available expressions.  | `axtonsemotes.expressions`        |
| `/jumpscares`<br>`/ae jumpscares`    | List all available jumpscares.  | `axtonsemotes.jumpscares`        |
| `/ae reload`      | Reload the plugin configuration. | `axtonsemotes.reload`<br>Default: op        |
|         | Notifies the user when the plugin has an update available.      | `axtonsemotes.updatenotify`


<br>
<img width="350" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/configuration.png">
<hr>

To customize the plugin, edit the `config.yml` file:

```yml
# >>>>>>>>>>>>>>>>>>>>>
# >> Global Configuration
# > Modify plugin configuration values and features:
# >>>>>>>>>>>>>>>>>>>>>

# Change between supported plugin languages located 
# in 'lang/', or create your own using 'en.yml'.
language: en # 'en' -> 'lang-en.yml'

# Toggles the ability for players to execute commands 
# on themselves, such as /hug axtonprice
allow-self-executions: true


# >>>>>>>>>>>>>>>>>>>>>
# >> Optional Features
# > Toggle optional plugin command features:
# >>>>>>>>>>>>>>>>>>>>>

# Should players be able to view a list of the 
# enabled emotes?
list-commands:
  enabled: true


# >>>>>>>>>>>>>>>>>>>>>
# >> Plugin Versioning
# > Configure version checking/notifications:
# >>>>>>>>>>>>>>>>>>>>>

# Should the plugin check for new updates?
check-for-latest: true

# Should players with the axtonsemotes.updatenotify 
# permission be notified of new updates?
notify-on-update: true

# The current version of the plugin
# --DO NOT MODIFY!--
version: 1.4.0 
```
After updating the configuration, reload it with `/ae reload`.

<br>
<img width="200" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/credits.png">
<hr>

## Main Author

### axtonprice ✨

[![GitHub](https://img.shields.io/badge/Github-%23121011.svg?logo=github&logoColor=white)](https://github.com/axtonprice) |  [![GitHub Sponsors](https://img.shields.io/discord/308323056592486420?logo=discord&%22%20alt=%22Chat%20on%20Discord%22)](https://discord.gg/dP3MuBATGc)
| [![Github-sponsors](https://img.shields.io/badge/sponsor-30363D?logo=GitHub-Sponsors&logoColor=#EA4AAA)](https://github.com/sponsors/axtonprice)

<br>
<img width="200" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/license.png">
<hr>

This project is MIT licensed. View the license [here](https://github.com/axtonprice/axtons-emotes/blob/main/LICENSE).

MIT © axtonprice