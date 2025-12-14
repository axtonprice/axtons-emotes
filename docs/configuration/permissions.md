# Permissions

The following permissions can be used to allow access to plugin commands.

#### User Permissions

- **axtonsemotes.emotes.\*** <br>
  Allows access to all emote-based user commands.
<br><br>

- **axtonsemotes.emotes.use** *(default: true)*  
  Allows players to execute emote commands.
<br><br>

- **axtonsemotes.emotes.list** *(default: true)*  
  Allows players to list permitted emote commands. 
<br><br>

- **axtonsemotes.emotes.selftoggle** *(default: true)*  
  Allows players to self-toggle shared emotes.
<br><br>

#### Admin Permissions

- **axtonsemotes.admin.\*** <br>
  Allows to access to all administrative commands.
<br><br>

- **axtonsemotes.admin.bypassradius** *(default: op)*  
  Allows players to bypass emote radius restrictions.
<br><br>

- **axtonsemotes.admin.bypasscooldown** *(default: op)*  
  Allows players to bypass emote cooldown restrictions.
<br><br>

- **axtonsemotes.admin.list-override** *(default: op)*  
  Allows players to list emotes, even when feature is disabled.
<br><br>

- **axtonsemotes.admin.version** *(default: op)*  
  Allows players to check the plugin version.
<br><br>

- **axtonsemotes.admin.reload** *(default: op)*  
  Allows players to reload the plugin configuration files.
<br><br>

- **axtonsemotes.admin.updatenotify** *(default: op)*  
  Allows players to receive notifications when they join if the plugin is out of date.
<br><br>

- **axtonsemotes.admin.toggledebug** *(default: op)*  
  Allows players to toggle debugging mode.
<br><br>

- **axtonsemotes.admin.togglemetrics** *(default: op)*  
  Allows players to toggle bStats anonymous metrics tracking.
<br><br>

- **axtonsemotes.admin.resetlang** *(default: op)*  
  Allows players to reset all language files to plugin defaults.
<br><br>

- **axtonsemotes.admin.resetemotes** *(default: op)*  
  Allows players to reset `emotes.yml` file to plugin defaults.
<br><br>

- **axtonsemotes.admin.help** *(default: op)*  
  Allows players to access the admin help command.
<br><br>

#### plugin.yml
Below displays the raw plugin permissions as specified in `plugin.yml`.

```yml
permissions:
  axtonsemotes.emotes.use:
    default: true
  axtonsemotes.emotes.list:
    default: true
  axtonsemotes.emotes.selftoggle:
    default: true
  axtonsemotes.admin.bypassradius:
    default: op
  axtonsemotes.admin.bypasscooldown:
    default: op
  axtonsemotes.admin.list-override:
    default: op
  axtonsemotes.admin.version:
    default: op
  axtonsemotes.admin.reload:
    default: op
  axtonsemotes.admin.updatenotify:
    default: op
  axtonsemotes.admin.toggledebug:
    default: op
  axtonsemotes.admin.togglemetrics:
    default: op
  axtonsemotes.admin.resetlang:
    default: op
  axtonsemotes.admin.resetemotes:
    default: op
  axtonsemotes.admin.help:
    default: op
```
