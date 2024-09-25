package one.tranic.mavenLoader.plugins;

import one.tranic.mavenloader.api.MavenLibraryResolver;
import one.tranic.mavenloader.boost.Boost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Loader {
    private final Resolver EMPTY_RESOLVER = new Resolver(new ArrayList<>(), new ArrayList<>());

    public Loader(@NotNull Path directory) throws Exception {
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

            MavenLibraryResolver r = new MavenLibraryResolver();

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
            return new Resolver(repository, dependency);
        }
    }

    private record Resolver(List<String> repository, List<String> dependency) {
    }
}
