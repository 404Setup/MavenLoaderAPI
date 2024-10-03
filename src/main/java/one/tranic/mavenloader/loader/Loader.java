package one.tranic.mavenloader.loader;

import one.tranic.mavenloader.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.slf4j.Logger;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Loader {
    private final Resolver EMPTY_RESOLVER = new Resolver(new ArrayList<>(), new ArrayList<>());
    private final Logger logger;

    public Loader(@NotNull Path directory, @NotNull Logger logger) throws Exception {
        this.logger = logger;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.jar")) {
            ArrayList<Resolver> resolvers = new ArrayList<>();

            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    Resolver res = checkForMavenYml(entry);
                    if (res != null && !res.dependency().isEmpty()) resolvers.add(res);
                }
            }

            Resolver resolver = clearResolver(resolvers);
            if (resolver == EMPTY_RESOLVER || resolver.dependency.isEmpty()) return;

            LibraryResolver r = new LibraryResolver();

            int i = 0;
            for (String repo : resolver.repository) {
                r.addRepository(repo, "AutoRepository-" + i);
                i++;
            }
            for (String dep : resolver.dependency) {
                r.addDependency(dep);
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
    private Resolver clearResolver(@NotNull List<Resolver> resolvers) {
        if (resolvers.isEmpty()) return EMPTY_RESOLVER;

        ArrayList<String> repos = new ArrayList<>();
        ArrayList<String> depends = new ArrayList<>();

        for (Resolver resolver : resolvers) {
            if (!resolver.repository.isEmpty()) {
                for (String repo : resolver.repository) {
                    if (!Boost.isCentral(repo) && !repos.contains(repo)) repos.add(repo);
                }
            }
            for (String depend : resolver.dependency) {
                if (!depends.contains(depend)) depends.add(depend);
            }
        }
        return new Resolver(repos, depends);
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
            List<String> dependency = c.getStringList("dependency");
            if (dependency.isEmpty()) return null;

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

            return new Resolver(repository, dependency);
        }
    }

    private record Resolver(List<String> repository, List<String> dependency) {
    }
}
