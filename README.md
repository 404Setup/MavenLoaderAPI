# MavenLoader
MavenLoader is used to solve the problem of lack of dependency loaders in Velocity.

It is now provided as a Bukkit plugin, and will provide BungeeCord support next time.

## Required
- Java 17+
- Velocity 3.3.0+ (Optional)
- Spigot/Paper/Folia 1.18.2-1.21.1 (Optional)

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
Docs in: [maven-loader-api](https://github.com/LevelTranic/MavenLoader-API)

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

```