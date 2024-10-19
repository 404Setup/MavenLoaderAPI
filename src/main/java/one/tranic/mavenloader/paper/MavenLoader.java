package one.tranic.mavenloader.paper;

import one.tranic.mavenloader.Platform;
import one.tranic.mavenloader.common.MavenLoaderUpdater;
import one.tranic.mavenloader.common.MessageSender;
import one.tranic.mavenloader.common.loader.Loader;
import one.tranic.mavenloader.common.updater.modrinth.source.Loaders;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MavenLoader extends JavaPlugin {
    private final Logger logger = LoggerFactory.getLogger("MavenLoaderAPI-"+Platform.get().toRawString());
    private Metrics metrics;

    @Override
    public void onEnable() {
        if (Platform.get() == Platform.Spigot)
            throw new RuntimeException("MavenLoaderAPI no longer provides Spigot compatibility, it brings too much trouble.");
        Loader.MainLoader(getDataFolder().toPath(), logger);
        logger.info("Initializing MavenLoaderAPI");
        metrics = new Metrics(this, 23501);
        MessageSender.setPlugin(this);
        new MavenLoaderUpdater(getDescription().getVersion(), getServer().getConsoleSender(), Loaders.of(Platform.get().toString())).checkUpdate();
    }

    @Override
    public void onDisable() {
        logger.info("Shutting down MavenLoaderAPI");
        if (metrics != null) {
            metrics.shutdown();
        }
        MessageSender.close();
    }

    /*private void checkUpdate() {
        if (!Config.isUpdaterCheck()) return;
        Pattern pattern = Pattern.compile("^\\d+\\.\\d+(?:\\.\\d+)?"); // Remove the content after 1.20.6 in 1.20.6-R0.1-SNAPSHOT (like this)
        Matcher matcher = pattern.matcher(getServer().getBukkitVersion());
        String version = matcher.find() ? matcher.group() : "1.21.1"; // By default, it is set to 1.21.1

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
        updater.getUpdateAsync((result) -> {
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
        });
    }*/
}
