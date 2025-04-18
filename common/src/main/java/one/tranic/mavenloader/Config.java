package one.tranic.mavenloader;

import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Config {
    private static File configFile;
    private static YamlConfiguration configuration;

    private static List<String> whitelistRepo;
    private static boolean enableWhitelist = true;
    private static boolean updateCheck = true;


    public static void loadConfig(@NotNull Path dataDirectory) {
        configFile = dataDirectory.getParent().resolve("MavenLoader").resolve("config.yml").toFile();
        try {
            if (!configFile.exists()) {
                if (!configFile.getParentFile().exists()) {
                    configFile.getParentFile().mkdir();
                }
                configFile.createNewFile();
            }
            configuration = YamlConfiguration.loadConfiguration(configFile);
            save();
            read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void read() {
        enableWhitelist = configuration.getBoolean("enable_whitelist");
        whitelistRepo = configuration.getStringList("whitelist");

        updateCheck = configuration.getBoolean("update_check");
    }

    private static void save() throws IOException {
        configuration.addDefault("enable_whitelist", true);
        List<String> list = Arrays.asList(
                "https://repo.maven.apache.org/maven2", // Central Repository
                "https://repo.maven.apache.org/maven2", // Central Repository
                "https://oss.sonatype.org/content/groups/public/", // Central Repository
                "https://repository.jboss.org/nexus/content/groups/public", // Central Repository Mirror - from Redhat
                // Central Repository Mirror - from Google - Start
                "https://maven-central-asia.storage-download.googleapis.com/maven2/",
                "https://maven-central-eu.storage-download.googleapis.com/maven2/",
                "https://maven-central.storage-download.googleapis.com/maven2/",
                // Central Repository Mirror - from Google - End
                "https://jitpack.io", // Jitpack Repository
                "https://hub.spigotmc.org/nexus/content/repositories/snapshots/", // Spigot Repository
                "https://repo.papermc.io/repository/maven-public/" // Paper Repository
        );
        configuration.addDefault("whitelist", list);

        configuration.addDefault("update_check", true);

        configuration.options().copyDefaults(true);
        configuration.save(configFile);
    }

    public static boolean getEnableWhitelist() {
        return enableWhitelist;
    }

    public static boolean isWhitelistRepo(String repo) {
        if (!enableWhitelist || whitelistRepo == null || whitelistRepo.isEmpty()) return true;
        return whitelistRepo.contains(repo);
    }

    public static boolean isUpdateCheck() {
        return updateCheck;
    }
}
