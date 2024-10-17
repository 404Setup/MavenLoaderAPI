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
import one.tranic.mavenloader.common.loader.Loader;
import one.tranic.mavenloader.common.updater.UpdateRecord;
import one.tranic.mavenloader.common.updater.UpdateSource;
import one.tranic.mavenloader.common.updater.Updater;
import one.tranic.mavenloader.common.updater.github.GithubUpdate;
import one.tranic.mavenloader.common.updater.hangar.HangarUpdate;
import one.tranic.mavenloader.common.updater.modrinth.ModrinthUpdate;
import one.tranic.mavenloader.common.updater.modrinth.source.Loaders;
import one.tranic.mavenloader.common.updater.spiget.SpigetUpdate;
import one.tranic.mavenloader.common.updater.spigot.SpigotUpdate;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = "maven-loader", name = "MavenLoader", version = BuildConstants.VERSION, url = "https://tranic.one", authors = {"404"})
public class MavenLoader {
    private static Updater updater;
    private final Logger logger;
    private final Metrics.Factory metricsFactory;
    private final @DataDirectory Path dataDirectory;
    private final ProxyServer proxy;
    private Metrics metrics;

    @Inject
    public MavenLoader(Logger logger, ProxyServer proxy, Metrics.Factory metricsFactory, @DataDirectory Path dataDirectory) {
        this.logger = logger;
        this.metricsFactory = metricsFactory;
        this.dataDirectory = dataDirectory;
        this.proxy = proxy;
        Loader.MainLoader(this.dataDirectory, this.logger);
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
        updater = switch (UpdateSource.of(Config.getUpdaterSource())) {
            case Github -> new GithubUpdate(BuildConstants.VERSION, "LevelTranic/MavenLoader");
            case Spigot -> new SpigotUpdate(BuildConstants.VERSION, 119660);
            case Spiget -> new SpigetUpdate(BuildConstants.VERSION, 119660);
            case Hangar ->
                    new HangarUpdate(BuildConstants.VERSION, "mavenloader-api", "https://hangar.papermc.io/Tranic/MavenLoaderAPI");
            case Modrinth -> new ModrinthUpdate("mavenloaderapi", BuildConstants.VERSION, Loaders.VELOCITY, "1.21.1");
            default ->
                    throw new RuntimeException("This updater channel: " + Config.getUpdaterSource() + " is not supported");
        };
        try {
            UpdateRecord result = updater.getUpdate();
            if (result != null) {
                if (result.hasUpdate()) {
                    ConsoleCommandSource source = proxy.getConsoleCommandSource();
                    source.sendMessage(Component.text("We found a MavenLoaderAPI updater!", NamedTextColor.BLUE));
                    source.sendMessage(Component.text("This machine Mavenloader version ", NamedTextColor.YELLOW)
                            .append(Component.text(BuildConstants.VERSION, NamedTextColor.AQUA))
                            .append(Component.text(", available updated version ", NamedTextColor.YELLOW))
                            .append(Component.text(result.newVersion(), NamedTextColor.AQUA))
                    );
                    source.sendMessage(Component.text("Update information: ", NamedTextColor.YELLOW));
                    source.sendMessage(Component.text(result.updateInfo()));
                    source.sendMessage(Component.text("Download and updater here: ", NamedTextColor.YELLOW)
                            .append(Component.text(result.updateUrl(), NamedTextColor.AQUA)));
                } else {
                    proxy.getConsoleCommandSource().sendMessage(Component.text("MavenloaderAPI is already the latest version!", NamedTextColor.GREEN));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
