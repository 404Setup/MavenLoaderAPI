# MavenLoader
MavenLoader is used to solve the problem of lack of dependency loaders in Velocity.

## Required
- Java 17+
- Velocity 3.3.0+

## Usage

### Velocity
You need to add the following parameters to the Java startup parameters to enable it normally.

```bash
--add-opens=java.base/java.net=ALL-UNNAMED
```

Then download MavenLoader and put it in the Velocity plugin directory.

It is recommended to keep MavenLoader the latest version.

### Developer
Docs in: [maven-loader-api](https://github.com/LevelTranic/MavenLoader-API)

## Tips
MavenLoaderAPI and MavenLoader-API are different projects.

### MavenLoader-API
The API for developers and its dependence should not be included in any plugin.

### MavenLoaderAPI
It is called MavenLoader in the GitHub project, and the Velocity plugin ID is `maven-loader`, which contains MavenLoader-API and its dependency items to avoid multiple plugin MavenLoader-API and dependencies cause operating problems.