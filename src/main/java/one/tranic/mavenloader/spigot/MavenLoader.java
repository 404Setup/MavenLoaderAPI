package one.tranic.mavenloader.spigot;

import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.plugins.Loader;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MavenLoader extends JavaPlugin {
    private final Logger logger = LoggerFactory.getLogger("MavenLoaderAPI");
    private Metrics metrics;

    public MavenLoader() {
        super();
        Config.loadConfig(getDataFolder().toPath());
        if (Config.getEnableWhitelist()) {
            logger.warn("The Maven repository white list has been enabled, and the repository on the white list can be loaded. ");
            logger.warn("If other plugins cannot be loaded, check the repository URL in the error and confirm whether they are on the whitelist.");
        }
        try {
            new Loader(getDataFolder().toPath().getParent(), logger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        logger.info("Initializing MavenLoader");
        metrics = new Metrics(this, 23501);
    }

    @Override
    public void onDisable() {
        if (metrics != null) {
            metrics.shutdown();
        }
    }
}
