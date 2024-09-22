package one.tranic.mavenLoader;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import one.tranic.mavenLoader.plugins.Loader;
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
        try {
            new Loader(dataDirectory.getParent());
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
