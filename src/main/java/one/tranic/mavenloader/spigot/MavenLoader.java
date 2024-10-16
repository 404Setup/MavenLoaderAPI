package one.tranic.mavenloader.spigot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.common.MessageSender;
import one.tranic.mavenloader.common.loader.Loader;
import one.tranic.mavenloader.common.update.UpdateRecord;
import one.tranic.mavenloader.common.update.UpdateSource;
import one.tranic.mavenloader.common.update.Updater;
import one.tranic.mavenloader.common.update.github.GithubUpdate;
import one.tranic.mavenloader.common.update.spiget.SpigetUpdate;
import one.tranic.mavenloader.common.update.spigot.SpigotUpdate;
import one.tranic.mavenloader.velocity.BuildConstants;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class MavenLoader extends JavaPlugin {
    private Updater updater;
    private final Logger logger = LoggerFactory.getLogger("MavenLoaderAPI");
    private Metrics metrics;

    @Override
    public void onEnable() {
        Loader.MainLoader(getDataFolder().toPath(), logger);
        logger.info("Initializing MavenLoaderAPI (Spigot)");
        metrics = new Metrics(this, 23501);
        MessageSender.setPlugin(this);
        checkUpdate();
    }

    @Override
    public void onDisable() {
        logger.info("Shutting down MavenLoaderAPI (Spigot)");
        if (metrics != null) {
            metrics.shutdown();
        }
        MessageSender.close();
    }

    private void checkUpdate() {
        if (!Config.isUpdaterCheck()) return;
        updater = switch (UpdateSource.of(Config.getUpdaterSource())) {
            case Github -> new GithubUpdate(getDescription().getVersion(), "LevelTranic/MavenLoader");
            case Spigot -> new SpigotUpdate(getDescription().getVersion(), 119660);
            case Spiget -> new SpigetUpdate(getDescription().getVersion(), 119660);
            default -> throw new RuntimeException("This update channel: "+Config.getUpdaterSource()+" is not supported");
        };
        try {
            UpdateRecord result = updater.getUpdate();
            if (result != null) {
                if (result.hasUpdate()) {
                    ConsoleCommandSender source = getServer().getConsoleSender();
                    MessageSender.sendMessage(Component.text("We found a MavenLoaderAPI update!", NamedTextColor.BLUE), source);
                    MessageSender.sendMessage(Component.text("This machine Mavenloader version ", NamedTextColor.YELLOW)
                                    .append(Component.text(BuildConstants.VERSION, NamedTextColor.AQUA))
                                    .append(Component.text(", available updated version ", NamedTextColor.YELLOW))
                                    .append(Component.text(result.newVersion(), NamedTextColor.AQUA))
                            , source
                    );
                    MessageSender.sendMessage(Component.text("Update information: ", NamedTextColor.YELLOW), source);
                    MessageSender.sendMessage(Component.text(result.updateInfo()), source);
                    MessageSender.sendMessage(Component.text("Download and update here: ", NamedTextColor.YELLOW)
                            .append(Component.text(result.updateUrl(), NamedTextColor.AQUA)), source);
                } else {
                    MessageSender.sendMessage(Component.text("MavenloaderAPI is already the latest version!", NamedTextColor.GREEN), getServer().getConsoleSender());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
