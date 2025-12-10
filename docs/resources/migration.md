# Migration

The plugin will **automatically** migrate configuration files when it detects they are outdated. It checks the installed plugin version against the `config-version` value in `config.yml`, and if the installed version is higher, a migration is triggered.

During migration, the plugin preserves all user-defined values. Updated configuration structures from newer versions are applied, but existing user-set keys are kept. Any missing keys are added with their default values, and any obsolete keys are removed.

Emote migration works similarly. For versions **1.5.0 and below**, `emotes.yml` (or legacy `commands.yml` for v1.4.0) cannot be migrated and will always be reset to its default state (legacy files will be removed and replaced). 

Versions **1.5.0 and 1.5.1** will be migrated to the new emote structure, and versions **1.6.0 and above** follow the same migration behaviour as `config.yml`, updating structural changes while preserving user values.

### Supported Versions

The table below summarises which versions support migration to the latest release:

| Version | `config.yml` | `emotes.yml` |
| ------------ | ------------ | ------------ |
| 1.3.0        | ❌          | ❌          |
| 1.4.0        | ✅          | ❌          |
| 1.5.0        | ✅          | ✅          |
| 1.5.1        | ✅          | ✅          |
| 1.6.0+       | ✅          | ✅          |

❌ indicates that migration is not supported for that file and upgrading will reset it to defaults.
<br>
✅ indicates that migration is supported and the file will be upgraded automatically.