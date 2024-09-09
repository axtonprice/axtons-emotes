<p>
    <img width="600" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/title.png">
</p>
<p>
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

<p>
  <img width="300" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/image1.png">
  <img width="300" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/image2.png">
</p>

<img width="350" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/installation.png">
<hr>

1. **Download**: Grab the latest '.jar' file from the [releases tab](https://github.com/axtonprice/axtons-emotes/releases).
2. **Deploy**: Place the downloaded file into your server's '/plugins' folder.
3. **Restart**: Restart your server. For optimal performance, using PaperMC is recommended.

> A minimum of Java 21 is required on the server to run this plugin.

<img width="230" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/features.png">
<hr>

### Emotes

Interact with other players using these emotes:

| Command              | Description                    | Permission Node            |
|----------------------|--------------------------------|----------------------------|
| '/dapup &lt;player&gt;' or '/dap &lt;player&gt;'    | Dap-up another player.         | 'axtonsemotes.dapup'       |
| '/highfive &lt;player&gt;' or '/hf &lt;player&gt;' | High-five another player.      | 'axtonsemotes.highfive'    |
| '/hug &lt;player&gt;'      | Hug another player.            | 'axtonsemotes.hug'         |
| '/kiss &lt;player&gt;' or '/smooch &lt;player&gt;'     | Kiss another player.           | 'axtonsemotes.kiss'        |
| '/poke &lt;player&gt;'     | Poke another player.           | 'axtonsemotes.poke'        |
| '/stomp &lt;player&gt;'    | Stomp on another player's foot.   | 'axtonsemotes.stomp'       |
| '/slap &lt;player&gt;'     | Slap another player.           | 'axtonsemotes.slap'        |
| '/stab &lt;player&gt;' or '/shank &lt;player&gt;'     | Stab another player.           | 'axtonsemotes.stab'        |
| '/stare &lt;player&gt;'    | Stare at another player.       | 'axtonsemotes.stare'       |
| '/thank &lt;player&gt;' or '/ty &lt;player&gt;'   | Thank another player.          | 'axtonsemotes.thank'       |
| '/applaud &lt;player&gt;' or '/clap &lt;player&gt;'  | Applaud another player.        | 'axtonsemotes.applaud'     |

### Expressions

Express yourself to all players on the server:

| Command   | Description                     | Permission Node            |
|-----------|---------------------------------|----------------------------|
| '/wave'   | Wave at all players.            | 'axtonsemotes.wave'        |
| '/cry' or '/sulk'    | Cry in front of all players.    | 'axtonsemotes.cry'         |

### Miscellaneous

Miscellaneous commands & extra features:

| Command           | Description                      | Permission Node            |
|-------------------|----------------------------------|----------------------------|
| '/bog'         | Jumpscare yourself!       | 'axtonsemotes.bog'        |
| '/emotes' or '/ae emotes'         | List all available emotes.       | 'axtonsemotes.emotes'        |
| '/expressions' or '/ae expressions'    | List all available expressions.  | 'axtonsemotes.expressions'        |
| '/jumpscares' or '/ae jumpscares'    | List all available jumpscares.  | 'axtonsemotes.jumpscares'        |
| '/ae reload'      | Reload the plugin configuration. | 'axtonsemotes.reload' Default: op        |

<img width="350" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/configuration.png">
<hr>

To customize the plugin, edit the 'config.yml' file:

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
version: 1.3.0 
```

After updating the configuration, reload it with '/ae reload'.

<img width="200" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/credits.png">

### Main Author

axtonprice ✨
[Github](https://github.com/axtonprice/axtons-emotes) | [Discord](https://discord.gg/dP3MuBATGc) | ❤ [Sponsor Me](https://github.com/sponsors/axtonprice)


<img width="200" src="https://github.com/axtonprice/axtons-emotes/raw/main/.github/media/license.png">

This project is MIT licensed. View the license [here](https://github.com/axtonprice/axtons-emotes/blob/main/LICENSE).

MIT © axtonprice