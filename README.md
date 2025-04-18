# MavenLoader
A custom external dependency loader is prepared to easily reduce the package size.

I consider it just an experimental project and not a good solution, so you should not use it, and my subsequent works will not use MavenLoaderAPI as a dependency.

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
- Paper/Folia 1.18.2 ↔ latest (Optional)

## Test Information
Versions starting from 1.18.2 should work fine.

if you have problems, please open an issues and give me the log and your environment information.

- Java 23 (Specified as 21 in Spigot and BungeeCord)
- Folia 1.21.1 latest
- ShreddedPaper 1.20.6 latest
- BungeeCord 1.21 a89cf5f:1869
- Velocity 3.3.0-SNAPSHOT git-2016d148-b436

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
update_check: true
```

## Statistics
- Velocity: https://bstats.org/plugin/velocity/MavenLoader/23396
- Paper: https://bstats.org/plugin/bukkit/MavenLoader-Spigot/23501
- Bungee: https://bstats.org/plugin/bungeecord/MavenLoader-Bungee/23524