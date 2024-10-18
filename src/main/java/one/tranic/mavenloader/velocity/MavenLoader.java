package one.tranic.mavenloader.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import one.tranic.mavenloader.common.MavenLoaderUpdater;
import one.tranic.mavenloader.common.loader.Loader;
import one.tranic.mavenloader.common.updater.modrinth.source.Loaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@Plugin(
        id = "maven-loader",
        name = "MavenLoader",
        version = BuildConstants.VERSION,
        description = "A custom external dependency loader is prepared to easily reduce the package size.",
        url = "https://tranic.one",
        authors = {"404"}
)
public class MavenLoader {
    private final Logger logger = LoggerFactory.getLogger("MavenLoaderAPI-Velocity");
    private final Metrics.Factory metricsFactory;
    private final @DataDirectory Path dataDirectory;
    private final ProxyServer proxy;
    private Metrics metrics;

    @Inject
    public MavenLoader(ProxyServer proxy, Metrics.Factory metricsFactory, @DataDirectory Path dataDirectory) {
        this.metricsFactory = metricsFactory;
        this.dataDirectory = dataDirectory;
        this.proxy = proxy;
        Loader.MainLoader(this.dataDirectory, this.logger);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Initializing MavenLoaderAPI");
        metrics = metricsFactory.make(this, 23396);
        new MavenLoaderUpdater(BuildConstants.VERSION, proxy.getConsoleCommandSource(), Loaders.VELOCITY).checkUpdate();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("Shutting down MavenLoaderAPI");
        if (metrics != null) {
            metrics.shutdown();
        }
    }
}
