package one.tranic.mavenloader.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.plugins.Loader;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "maven-loader", name = "MavenLoader", version = BuildConstants.VERSION, url = "https://tranic.one", authors = {"404"})
public class MavenLoader {
    private final Metrics.Factory metricsFactory;
    private final Logger logger;
    private Metrics metrics;

    @Inject
    public MavenLoader(Logger logger, Metrics.Factory metricsFactory, @DataDirectory Path dataDirectory) {
        this.logger = logger;
        this.metricsFactory = metricsFactory;
        Config.loadConfig(dataDirectory);
        if (Config.getEnableWhitelist()) {
            logger.warn("********    MavenLoaderAPI  WARN   ********");
            logger.warn("The Maven repository white list has been enabled, and the repository on the white list can be loaded. ");
            logger.warn("If other plugins cannot be loaded, check the repository URL in the error and confirm whether they are on the whitelist.");
            logger.warn("*******************************************");
        }
        try {
            new Loader(dataDirectory.getParent(), logger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Initializing MavenLoader");
        metrics = metricsFactory.make(this, 23396);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (metrics != null) {
            metrics.shutdown();
        }
    }
}
