<p align="center">
    <img width="600" src=".git_media/title.png">
</p>
<p align="center">
    <a href="https://discord.gg/dP3MuBATGc"><img src="https://img.shields.io/discord/308323056592486420?logo=discord&logoColor=white%22%20alt=%22Chat%20on%20Discord%22"></a>
    <a href="https://github.com/axtonprice/axtons-emotes/releases"><img src="https://img.shields.io/github/release/axtonprice/axtons-emotes?include_prereleases=&sort=semver&color=blue"></a>
    <a href="https://github.com/axtonprice/axtons-emotes/LICENSE"><img src="https://img.shields.io/badge/License-MIT-blue"></a>
    <a href="https://modrinth.com/plugin/axtonsemotes"><img src="https://img.shields.io/badge/Available_On_Modrinth-green"></a>
</p>

# Axton's Emotes
> A lightweight social emotes plugin for Bukkit/Spigot/Paper 1.21

<p align="center">
<img width="300" src=".git_media/image1.png">
<img width="300" src=".git_media/image2.png">
</p>

<br>
<img width="350" src=".git_media/installation.png">
<hr>

1. **Download**: Grab the latest `.jar` file from the [releases tab](https://github.com/axtonprice/axtons-emotes/releases).
2. **Deploy**: Place the downloaded file into your server's `/plugins` folder.
3. **Restart**: Restart your server. For optimal performance, using PaperMC is recommended.

> A minimum of Java 21 is required on the server to run this plugin.

<br>
<img width="230" src=".git_media/features.png">
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
| `/bog`         | Jumpscare yourself!       | `axtonsemotes.bog`        |
| `/emotes`<br>`/ae emotes`         | List all available emotes.       | `axtonsemotes.emotes`        |
| `/expressions`<br>`/ae expressions`    | List all available expressions.  | `axtonsemotes.expressions`        |
| `/ae reload`      | Reload the plugin configuration. | `axtonsemotes.reload`<br>Default: op        |


<br>
<img width="350" src=".git_media/configuration.png">
<hr>

To customize the plugin, edit the `config.yml` file:

```yml
# Enables or disables the ability for players to execute emote commands on themselves.
allowSelfExecutions: true

# Enable/disable specific commands
commands:
  # Expressions
  applaud:
    enabled: true
  cry:
    enabled: true
  sulk:
    enabled: true
  wave:
    enabled: true

  # Shared emotes
  dapup:
    enabled: true
  highfive:
    enabled: true
  hug:
    enabled: true
  kiss:
    enabled: true
  poke:
    enabled: true
  slap:
    enabled: true
  stab:
    enabled: true
  stare:
    enabled: true
  stomp:
    enabled: true
  thank:
    enabled: true

  # Misc Commands
  emotes:
    enabled: true
  expressions:
    enabled: true
  bog:
    enabled: true

```
After updating the configuration, reload it with `/ae reload`.

<br>
<img width="200" src=".git_media/credits.png">
<hr>

### Main Author

axtonprice ✨ <br>
[Github](https://github.com/axtonprice/axtons-emotes) | [Discord](https://discord.gg/dP3MuBATGc) | Donate *(Coming Soon)*

<br>
<img width="200" src=".git_media/license.png">
<hr>

This project is MIT licensed. View the license [here](https://github.com/axtonprice/axtons-emotes/LICENSE).

MIT © axtonprice