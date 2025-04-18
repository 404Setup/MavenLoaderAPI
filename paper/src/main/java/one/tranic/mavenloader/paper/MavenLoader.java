package one.tranic.mavenloader.paper;

import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.loader.Loader;
import one.tranic.t.base.TBase;
import one.tranic.t.base.updater.SpigotVersionFetcher;
import one.tranic.t.base.updater.VersionFetcher;
import one.tranic.t.paper.TPaper;
import one.tranic.t.utils.Platform;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MavenLoader extends JavaPlugin {
    private final Logger logger = LoggerFactory.getLogger("MavenLoaderAPI");
    private final boolean isPaper = Platform.get() == Platform.Paper || Platform.get() == Platform.ShreddedPaper || Platform.get() == Platform.Folia;
    private VersionFetcher fetcher;
    private Metrics metrics;

    @Override
    public void onEnable() {
        if (!isPaper)
            throw new IllegalStateException("MavenLoaderAPI is not compatible with Spigot");

        Loader.MainLoader(getDataFolder().toPath(), logger);
        logger.info("Initializing MavenLoaderAPI");
        metrics = new Metrics(this, 23501);

        TPaper.init(this);

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
        TPaper.disable();
    }
}
