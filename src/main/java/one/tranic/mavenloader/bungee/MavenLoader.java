package one.tranic.mavenloader.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import one.tranic.mavenloader.common.MessageSender;
import one.tranic.mavenloader.common.loader.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MavenLoader extends Plugin {
    private final Logger logger = LoggerFactory.getLogger("MavenLoaderAPI");
    private Metrics metrics;

    public MavenLoader() {
        super();
        Loader.MainLoader(getDataFolder().toPath(), logger);
    }

    @Override
    public void onEnable() {
        logger.info("Initializing MavenLoaderAPI (BungeeCord)");
        metrics = new Metrics(this, 23524);
        MessageSender.setPlugin(this);
    }

    @Override
    public void onDisable() {
        logger.info("Shutting down MavenLoaderAPI (BungeeCord)");
        if (metrics != null) {
            metrics.shutdown();
        }
        MessageSender.close();
    }
}
