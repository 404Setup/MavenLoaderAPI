package one.tranic.mavenloader.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.loader.Loader;
import one.tranic.mavenloader.update.UpdateRecord;
import one.tranic.mavenloader.update.Updater;
import one.tranic.mavenloader.update.github.GithubUpdate;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@Plugin(id = "maven-loader", name = "MavenLoader", version = BuildConstants.VERSION, url = "https://tranic.one", authors = {"404"})
public class MavenLoader {
    private final Logger logger;
    private final Metrics.Factory metricsFactory;
    private final @DataDirectory Path dataDirectory;
    private final ProxyServer proxy;
    private Metrics metrics;
    private static Updater updater;
    private static UpdateRecord updateRecord;

    @Inject
    public MavenLoader(Logger logger, ProxyServer proxy , Metrics.Factory metricsFactory, @DataDirectory Path dataDirectory) {
        this.logger = logger;
        this.metricsFactory = metricsFactory;
        this.dataDirectory = dataDirectory;
        this.proxy = proxy;
        Loader.MainLoader(this.dataDirectory, this.logger);
    }

    public static UpdateRecord getUpdateRecord() {
        return updateRecord;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Initializing MavenLoaderAPI (Velocity)");
        metrics = metricsFactory.make(this, 23396);
        checkUpdate();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("Shutting down MavenLoaderAPI (Velocity)");
        if (metrics != null) {
            metrics.shutdown();
        }
    }

    private void checkUpdate() {
        if (!Config.isUpdaterCheck()) return;
        if (Objects.equals(Config.getUpdaterSource(), "github")) {
            updater = new GithubUpdate(BuildConstants.VERSION);
        }
        new Thread(() -> {
            if (updater == null) return;
            try {
                UpdateRecord result = updater.getUpdate();
                if (result != null) {
                    if (result.hasUpdate()) {
                        updateRecord = result;
                        ConsoleCommandSource source = proxy.getConsoleCommandSource();
                        source.sendMessage(Component.text("We found a MavenLoaderAPI update!", NamedTextColor.BLUE));
                        source.sendMessage(Component.text("This machine Mavenloader version ", NamedTextColor.YELLOW)
                                .append(Component.text(BuildConstants.VERSION, NamedTextColor.AQUA))
                                .append(Component.text(", available updated version ", NamedTextColor.YELLOW))
                                .append(Component.text(result.newVersion(), NamedTextColor.AQUA))
                        );
                        source.sendMessage(Component.text("Update information: ", NamedTextColor.YELLOW));
                        source.sendMessage(Component.text(result.updateInfo()));
                        source.sendMessage(Component.text("Download and update here: ", NamedTextColor.YELLOW)
                                .append(Component.text(result.updateUrl(), NamedTextColor.AQUA)));
                    } else {
                        proxy.getConsoleCommandSource().sendMessage(Component.text("MavenloaderAPI is already the latest version!", NamedTextColor.GREEN));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
