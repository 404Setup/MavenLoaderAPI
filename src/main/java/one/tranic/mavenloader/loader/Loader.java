package one.tranic.mavenloader.loader;

import com.google.gson.Gson;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Loader {
    private final Logger logger;

    public Loader(@NotNull Path directory, @NotNull Logger logger) throws Exception {
        this.logger = logger;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.jar")) {
            List<Resolver> resolvers = new ArrayList<>();

            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    Resolver res = checkForMavenYml(entry);
                    if (res != null && !res.dependency().isEmpty()) resolvers.add(res);
                }
            }

            @NotNull List<String> repositorys = clearRepository(resolvers);

            LibraryResolver r = new LibraryResolver();

            int i = 0;
            for (String repo : repositorys) {
                r.addRepository(repo, "AutoRepository-" + i);
                i++;
            }

            for (Resolver resolver : resolvers) {
                if (resolver.isReferenceLoad()) {
                    for (String depend : resolver.dependency()) {
                        r.addDependency(depend, resolver.pluginClass());
                    }
                } else {
                    for (String depend : resolver.dependency()) {
                        r.addDependency(depend);
                    }
                }
            }
        }
    }

    public static void MainLoader(@NotNull Path directory, @NotNull Logger logger) {
        Config.loadConfig(directory);
        if (Config.getEnableWhitelist()) {
            logger.warn("********    MavenLoaderAPI  WARN   ********");
            logger.warn("The Maven repository white list has been enabled, and the repository on the white list can be loaded. ");
            logger.warn("If other plugins cannot be loaded, check the repository URL in the error and confirm whether they are on the whitelist.");
            logger.warn("*******************************************");
        }
        try {
            new Loader(directory.getParent(), logger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private List<String> clearRepository(@NotNull List<Resolver> resolvers) {
        ArrayList<String> repos = new ArrayList<>();
        for (Resolver resolver : resolvers) {
            if (resolver.repository.isEmpty()) continue;
            for (String repo : resolver.repository) {
                if (!Boost.isCentral(repo) && !repos.contains(repo)) repos.add(repo);
            }
        }
        return repos;
    }

    @Nullable
    private Resolver checkForMavenYml(@NotNull Path jarPath) throws Exception {
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            ZipEntry mavenYmlEntry = jarFile.getEntry("maven.yml");
            if (mavenYmlEntry != null && !mavenYmlEntry.isDirectory()) {
                return parseMavenYml(jarFile, mavenYmlEntry);
            }
            return null;
        }
    }

    @Nullable
    private Resolver parseMavenYml(@NotNull JarFile jarFile, @NotNull ZipEntry entry) throws Exception {
        try (var inputStream = jarFile.getInputStream(entry)) {
            YamlConfiguration c = YamlConfiguration.loadConfiguration(inputStream);

            boolean referenceLoad = c.getBoolean("reference-load"); // If it is enabled,
            // the dependencies will be loaded to the main class of the plugin instead of the master class of Mavenloader.

            List<String> pList = c.getStringList("platform." + Platform.get().toString()); // Loading dependencies independently on some platforms

            List<String> dependency = c.getStringList("dependency"); // Dependencies that will be installed on all platforms
            if (dependency.isEmpty())
                if (pList.isEmpty()) return null;

            dependency.addAll(pList);

            List<String> repository = c.getStringList("repository");

            if (Config.getEnableWhitelist() && !repository.isEmpty()) {
                List<String> notWhiteList = new ArrayList<>();
                for (String repo : repository) {
                    if (!Config.isWhitelistRepo(repo)) notWhiteList.add(repo);
                }
                if (!notWhiteList.isEmpty()) {
                    logger.warn("The plugin " + jarFile.getName() + " calls the Maven repository that is not in the whitelist, and it has been rejected.");
                    logger.warn("List of the repository of the plugin " + jarFile.getName() + ":" + repository);
                    logger.warn("The plugin " + jarFile.getName() + " is not on the repository in the whitelist:" + notWhiteList);
                    return null;
                }
            }

            return new Resolver(repository, dependency, referenceLoad, getPluginClass(jarFile));
        }
    }

    @Nullable
    private Class<?> getPluginClass(@NotNull JarFile file) {
        Platform p = Platform.get();
        return switch (p) {
            case Velocity -> getVelocityClass(file);
            case Folia, Spigot, ShreddedPaper -> getBukkitClass(file);
            case Paper -> getPaperClass(file);
            case BungeeCord -> getBungeeClass(file);
        };
    }

    @Nullable
    private Class<?> getVelocityClass(@NotNull JarFile file) {
        ZipEntry entry = file.getEntry("velocity-plugin.json");
        if (entry != null && !entry.isDirectory()) {
            try (var inputStream = file.getInputStream(entry)) {
                Gson gson = new Gson();
                try (var reader = new InputStreamReader(inputStream)) {
                    VelocityPlugin vc = gson.fromJson(reader, VelocityPlugin.class);
                    if (vc == null) return null;
                    return vc.getMainClass();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Nullable
    private Class<?> getYmlClass(@NotNull JarFile file, @NotNull String name) {
        ZipEntry entry = file.getEntry(name);
        if (entry != null && !entry.isDirectory()) {
            return getYmlClass(file, entry);
        }
        return null;
    }

    @Nullable
    private Class<?> getYmlClass(@NotNull JarFile file, @NotNull ZipEntry entry) {
        try (var inputStream = file.getInputStream(entry)) {
            YamlConfiguration c = YamlConfiguration.loadConfiguration(inputStream);
            try {
                return Class.forName(c.getString("main"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    private Class<?> getPaperClass(@NotNull JarFile file) {
        ZipEntry entry = file.getEntry("paper-plugin.yml");
        if (entry != null && !entry.isDirectory()) {
            return getYmlClass(file, entry);
        }
        return null;
    }

    @Nullable
    private Class<?> getBukkitClass(@NotNull JarFile file) {
        return getYmlClass(file, "plugin.yml");
    }

    @Nullable
    private Class<?> getBungeeClass(@NotNull JarFile file) {
        return getYmlClass(file, "bungee.yml");
    }

    private record Resolver(List<String> repository, List<String> dependency, boolean isReferenceLoad,
                            Class<?> pluginClass) {
    }
}
