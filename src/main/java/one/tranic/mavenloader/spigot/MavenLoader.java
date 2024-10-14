package one.tranic.mavenloader.spigot;

import one.tranic.mavenloader.common.MessageSender;
import one.tranic.mavenloader.common.loader.Loader;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MavenLoader extends JavaPlugin {
    private final Logger logger = LoggerFactory.getLogger("MavenLoaderAPI");
    private Metrics metrics;

    @Override
    public void onEnable() {
        Loader.MainLoader(getDataFolder().toPath(), logger);
        logger.info("Initializing MavenLoaderAPI (Spigot)");
        metrics = new Metrics(this, 23501);
        MessageSender.setPlugin(this);
    }

    @Override
    public void onDisable() {
        logger.info("Shutting down MavenLoaderAPI (Spigot)");
        if (metrics != null) {
            metrics.shutdown();
        }
        MessageSender.close();
    }
}
