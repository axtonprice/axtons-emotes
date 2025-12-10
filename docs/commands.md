# Commands

Below is an overview of plugin commands, as well as a list of command details.

## Command Overview

| Command | Description | Aliases |
| --- | --- | --- |
| `/emote`               | Execute emote command.         | `/e` |
| `/emotes`              | List emote commands.           | `/listemotes` |
| `/expressions`         | List emote commands.           | `/listexpressions`, `/reactions` |
| `/toggleemotes`        | Toggle shared emotes.          | `/emotestoggle` |
| `/ae`, `/axtonsemotes`  | Main plugin utility commands   |  |
| `/ae reload`            | Reload plugin config.          |  |
| `/ae version`           | Check for plugin updates.      |  |
| `/ae toggledebug`       | Toggle debug mode.             |  |
| `/ae togglemetrics`     | Toggle bStats metrics.         |  |
| `/ae resetlang`         | Reset language files.          |  |
| `/ae resetemotes`       | Resets emote.yml file.         |  |
| `/ae help`              | Provides link to wiki.         |  |

## Command Details
Detailed list of commands including parameters and aliases.

---

#### /emote
Allows players to execute emote commands.

**Usage**
```
/emote <emote> <player>
```

> The `player` parameter is ignored for expression emotes.

**Parameters**

| Parameter | Description |
| --- | --- |
| `<emote>` | Specify the name of the emote to execute. |
| `<player>` | Specify the player to execute the emote on (shared emotes only). |

---

#### /emotes
Allows players to list shared emote commands they are permitted to execute.

---

#### /expressions
Allows players to list expression emote commands they are permitted to execute.

---

#### /toggleemotes
Allows a player to enable or disable shared emotes for themselves. When disabled, they can't receive shared emotes from others, and they also can't initiate shared emotes with other players.

---

#### /ae
Base utility command. Requires subcommand to be specified.

---

#### /ae reload
Reloads the plugin configuration files, including `config.yml`, `emotes.yml`, and language files.

---

#### /ae version
Checks for plugin updates against the currently installed version. Uses GitHub releases to determine the latest version.

---

#### /ae toggledebug
Toggles debug messages from appearing in console. This provides additional information when plugin commands are executed.

---

#### /ae togglemetrics
Toggles bStats anonymous metrics tracking. Learn more about bStats at [bstats.org](https://bstats.org/plugin/bukkit/AxtonsEmotes/23323).

---

#### /ae resetlang
Resets all language files to plugin defaults. This includes the base `en.yml` file, and all translation files in the `translations/` folder.

---

#### /ae resetemotes
Resets the `emotes.yml` file to plugin defaults.

---

#### /ae help
Provides a link to the plugin wiki on the project GitHub repository.