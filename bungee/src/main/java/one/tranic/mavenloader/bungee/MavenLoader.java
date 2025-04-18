package one.tranic.mavenloader.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.loader.Loader;
import one.tranic.t.base.TBase;
import one.tranic.t.base.updater.SpigotVersionFetcher;
import one.tranic.t.base.updater.VersionFetcher;
import one.tranic.t.bungee.TBungee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public final class MavenLoader extends Plugin {
    private final Logger logger = LoggerFactory.getLogger("MavenLoaderAPI");
    private VersionFetcher fetcher;
    private Metrics metrics;

    public MavenLoader() {
        super();
        Loader.MainLoader(getDataFolder().toPath(), logger);
    }

    @Override
    public void onEnable() {
        logger.info("Initializing MavenLoaderAPI");
        metrics = new Metrics(this, 23524);

        TBungee.init(this);

        TBase.runAsync(() -> {
            if (Config.isUpdateCheck()) {
                fetcher = new SpigotVersionFetcher(getDescription().getVersion(), "MavenLoaderAPI", "119660");
                if (fetcher.hasUpdate()) TBase.getConsoleSource().sendMessage(fetcher.getUpdateMessage());
                fetcher.start();
            }
        });
    }

    @Override
    public void onDisable() {
        logger.info("Shutting down MavenLoaderAPI");
        if (metrics != null) metrics.shutdown();

        if (fetcher != null) {
            fetcher.close();
            fetcher = null;
        }

        TBase.close();
        TBungee.disable();
    }
}
