# MavenLoader
A custom external dependency loader is prepared to easily reduce the package size.

<div align=center>
<img src="https://badges.moddingx.org/modrinth/downloads/mavenloader-api" alt="">
<img src="https://badges.moddingx.org/modrinth/versions/mavenloader-api" alt="">
<img src="https://badges.moddingx.org/curseforge/downloads/1104872" alt="">
<img src="https://badges.moddingx.org/curseforge/versions/1104872" alt="">
<img src="https://pluginbadges.glitch.me/api/v1/dl/MavenLoaderAPI+Spigot-limegreen.svg?spigot=mavenloaderapi.119660&bukkit=mavenloaderapi&github=LevelTranic/MavenLoader&style=for-the-badge" alt="">
<img src="https://img.shields.io/github/commit-activity/w/LevelTranic/MavenLoader?style=flat-square" alt="">
<img src="https://img.shields.io/github/downloads/LevelTranic/MavenLoader/total?style=flat-square" alt="">
<img src="https://img.shields.io/github/release-date/LevelTranic/MavenLoader?style=flat-square" alt="">
<img src="https://img.shields.io/github/v/release/LevelTranic/MavenLoader?style=flat-square" alt="">
</div>

## Required
- Java 17+
- Velocity 3.3.0 ↔ latest (Optional)
- BungeeCord 1.18 ↔ latest (Optional)
- Spigot/Paper/Folia 1.18.2 ↔ latest (Optional)

## Plugin Compatibility
- PlugManX (1.6-SNAPSHOT will not be provided, and it may be provided in future updates.)

## Download
All places use the same build pack, I just left these links to let you know I'm only releasing it in these places.

- **Github**: https://github.com/LevelTranic/MavenLoader/releases
- **Spigot**: https://www.spigotmc.org/resources/mavenloaderapi.119660/
- **CurseForge**: https://www.curseforge.com/minecraft/bukkit-plugins/mavenloaderapi
- **Modrinth**: https://modrinth.com/plugin/mavenloader-api
- **Hangar**: https://hangar.papermc.io/Tranic/MavenLoaderAPI

## Usage
1.4-SNAPSHOT introduced a repository whitelist mechanism to try to avoid some untrusted repositories from loading malicious dependencies.

### Server
You need to add the following parameters to the Java startup parameters to enable it normally.

```bash
--add-opens=java.base/java.net=ALL-UNNAMED
```

Then download MavenLoader and put it in the Server plugin directory. It is recommended to keep MavenLoader the latest version.

**Please confirm whether the plugin that relies on MavenLoaderAPI loads trusted dependencies and pay attention to safety.**

### Developer
Docs in: [DEVELOPER_DOCS](DEVELOPER_DOCS.md)

Updater API in: [USE_UPDATER](USE_UPDATER.md)

Example in: [MavenLoader-Example](https://github.com/LevelTranic/MavenLoader-Example)

## Compatibility
- 1.2-SNAPSHOT: Implementing `maven.yml`
- 1.4-SNAPSHOT: Implementing repository whitelist
- 1.5-SNAPSHOT: Support Bukkit
- 1.6-SNAPSHOT (Coming soon): Support BungeeCord. `maven.yml` support `platform`, `reference-load` keyword. Remove external API (now only allow maven.yml)

## Config
```yaml
enable_whitelist: true # Enable repository whitelist, default is true
# repository whitelist list
whitelist:
  - https://repo.maven.apache.org/maven2
  - https://repo.maven.apache.org/maven2
  - https://oss.sonatype.org/content/groups/public/
  - https://repository.jboss.org/nexus/content/groups/public
  - https://maven-central-asia.storage-download.googleapis.com/maven2/
  - https://maven-central-eu.storage-download.googleapis.com/maven2/
  - https://maven-central.storage-download.googleapis.com/maven2/
  - https://jitpack.io
  - https://hub.spigotmc.org/nexus/content/repositories/snapshots/
  - https://repo.papermc.io/repository/maven-public/
  - https://repo.repsy.io/mvn/rdb/default
updater:
  check: true
  source: github # Update Channel, Support: github, spigot, spiget, modrinth, hangar
  simple-mode: true # Sacrifice some detection accuracy in exchange for some performance. (default: true)
```

## Statistics
- Velocity: https://bstats.org/plugin/velocity/MavenLoader/23396
- Spigot: https://bstats.org/plugin/bukkit/MavenLoader-Spigot/23501
- Bungee: https://bstats.org/plugin/bungeecord/MavenLoader-Bungee/23524