package one.tranic.mavenloader.spigot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.common.MessageSender;
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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MavenLoader extends JavaPlugin {
    private final Logger logger = LoggerFactory.getLogger("MavenLoaderAPI");
    private Updater updater;
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
        Pattern pattern = Pattern.compile("^\\d+\\.\\d+(?:\\.\\d+)?");
        Matcher matcher = pattern.matcher(getServer().getBukkitVersion());
        String version = matcher.find() ? matcher.group() : "1.21.1";

        updater = switch (UpdateSource.of(Config.getUpdaterSource())) {
            case Github -> new GithubUpdate(getDescription().getVersion(), "LevelTranic/MavenLoader");
            case Spigot -> new SpigotUpdate(getDescription().getVersion(), 119660);
            case Spiget -> new SpigetUpdate(getDescription().getVersion(), 119660);
            case Modrinth ->
                    new ModrinthUpdate("mavenloader-api", getDescription().getVersion(), Loaders.SPIGOT, version);
            case Hangar ->
                    new HangarUpdate(getDescription().getVersion(), "mavenloaderapi", "https://hangar.papermc.io/Tranic/MavenLoaderAPI");
            default ->
                    throw new RuntimeException("This updater channel: " + Config.getUpdaterSource() + " is not supported");
        };
        try {
            UpdateRecord result = updater.getUpdate();
            if (result != null) {
                if (result.hasUpdate()) {
                    ConsoleCommandSender source = getServer().getConsoleSender();
                    MessageSender.sendMessage(Component.text("We found a MavenLoaderAPI updater!", NamedTextColor.BLUE), source);
                    MessageSender.sendMessage(Component.text("This machine Mavenloader version ", NamedTextColor.YELLOW)
                                    .append(Component.text(getDescription().getVersion(), NamedTextColor.AQUA))
                                    .append(Component.text(", available updated version ", NamedTextColor.YELLOW))
                                    .append(Component.text(result.newVersion(), NamedTextColor.AQUA))
                            , source
                    );
                    MessageSender.sendMessage(Component.text("Update information: ", NamedTextColor.YELLOW), source);
                    MessageSender.sendMessage(Component.text(result.updateInfo()), source);
                    MessageSender.sendMessage(Component.text("Download and updater here: ", NamedTextColor.YELLOW)
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
