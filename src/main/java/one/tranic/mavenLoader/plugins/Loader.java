package one.tranic.mavenLoader.plugins;

import one.tranic.mavenloader.api.MavenLibraryResolver;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Loader {
    public void scanJarFiles(Path directory) throws Exception {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.jar")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    checkForMavenYml(entry);
                }
            }
        }
    }

    private void checkForMavenYml(Path jarPath) throws Exception {
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            ZipEntry mavenYmlEntry = jarFile.getEntry("maven.yml");
            if (mavenYmlEntry != null && !mavenYmlEntry.isDirectory()) {
                parseMavenYml(jarFile, mavenYmlEntry);
            }
        }
    }

    private void parseMavenYml(JarFile jarFile, ZipEntry entry) throws Exception {
        try (var inputStream = jarFile.getInputStream(entry)) {
            YamlConfiguration c = YamlConfiguration.loadConfiguration(inputStream);
            List<String> repository = c.getStringList("repository");
            List<String> dependency = c.getStringList("dependency");
            if (dependency.isEmpty()) return;
            MavenLibraryResolver resolver = new MavenLibraryResolver();
            if (!repository.isEmpty()) {
                int i = 0;
                for (String repo : repository) {
                    resolver.addRepository(repo, "AutoRepository-" + i);
                    i++;
                }
            }
            for (String dep : dependency) {
                resolver.addDependency(dep);
            }
        }
    }
}
