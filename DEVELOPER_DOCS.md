# Developer Docs

**The API no longer allows references, you should use maven.yml instead.**

We have already added the central repository and sonatype by default, no need to add them further.

Example in: [MavenLoader-Example](https://github.com/LevelTranic/MavenLoader-Example)

## Usage

### Set hard dependence
#### Velocity (Java)
```java
@Plugin(
    id = "my-plugin", 
    name = "MyPlugin", 
    version = BuildConstants.VERSION, 
    dependencies = {@Dependency(id = "maven-loader")}
)
public class MyPlugin {
    
}
```

#### Velocity (Kotlin)
```kotlin
@Plugin(
    id = "my-plugin",
    name = "MyPlugin",
    version = BuildConstants.VERSION,
    dependencies = [Dependency(id = "maven-loader")]
)
class MavenLoader {
}
```

#### Spigot
`plugin.yml`
```yaml
name: ExamplePlugin
version: '1.0-SNAPSHOT'
main: com.example.Main
api-version: '1.18'
load: STARTUP
authors: [ "Me" ]
folia-supported: true
depend:
  - MavenLoader
```

#### BungeeCord
`bungee.yml`
```yaml
name: ExamplePlugin
version: '1.0-SNAPSHOT'
main: com.example.Main
authors: [ "Me" ]
depend:
  - MavenLoader
```


### Setting dependencies
MavenLoader `1.2-SNAPSHOT` introduced a new loading mechanism.

You only need to create a `maven.yml` in `src/main/resources`

Support for loading dependencies for specific platforms has been added since `1.6-SNAPSHOT`.

`dependency` in dependency will be loaded everywhere, while dependencies in platform will only be loaded on specific platforms.

```yaml
# If this option is enabled, MavenLoader will try to load dependencies into the plugin's class instead of the MavenLoader class. 
# This can solve some problems due to class loader conflicts, such as MiniMessage in Spigot.
reference-load: true
repository:
  - https://jitpack.io
dependency:
  - net.kyori:adventure-text-minimessage:4.17.0
  # Simple-YAML should not be added, as MavenLoaderAPI already includes that dependency since 1.2-SNAPSHOT.
platform: # Supported keywords: spigot, paper, folia, shreddedpaper, bungeecord, velocity. 
  # Other forks will be processed according to their upstreams.
  spigot:
    - net.kyori:adventure-platform-bukkit:4.3.4
  paper:
    - other depend
  folia:
    - other depend2
  shreddedpaper:
    - other depend3
  bungeecord:
    - net.kyori:adventure-platform-bungeecord:4.3.4
  velocity:
    - other depend5
```

Just like this.