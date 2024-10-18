package one.tranic.mavenloader.bungee;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.common.MessageSender;
import one.tranic.mavenloader.common.updater.UpdateSource;
import one.tranic.mavenloader.common.updater.Updater;
import one.tranic.mavenloader.common.updater.github.GithubUpdate;
import one.tranic.mavenloader.common.updater.hangar.HangarUpdate;
import one.tranic.mavenloader.common.updater.modrinth.ModrinthUpdate;
import one.tranic.mavenloader.common.updater.modrinth.source.Loaders;
import one.tranic.mavenloader.common.updater.spiget.SpigetUpdate;
import one.tranic.mavenloader.common.updater.spigot.SpigotUpdate;

public class UpdateUtils {
    private static Updater updater;
    private static String version;

    public static void setVersion(String version) {
        UpdateUtils.version = version;
    }

    public static void checkUpdate(ProxyServer server) {
        if (!Config.isUpdaterCheck()) return;
        updater = switch (UpdateSource.of(Config.getUpdaterSource())) {
            case Github -> new GithubUpdate(version, "LevelTranic/MavenLoader");
            case Spigot -> new SpigotUpdate(version, 119660);
            case Spiget -> new SpigetUpdate(version, 119660);
            case Modrinth -> new ModrinthUpdate("mavenloader-api", version, Loaders.BUNGEECORD, "1.21.1");
            case Hangar ->
                    new HangarUpdate(version, "mavenloaderapi", "https://hangar.papermc.io/Tranic/MavenLoaderAPI");
            default ->
                    throw new RuntimeException("This updater channel: " + Config.getUpdaterSource() + " is not supported");
        };
        updater.getUpdateAsync((result) -> {
            if (result != null) {
                if (result.hasUpdate()) {
                    CommandSender source = server.getConsole();
                    MessageSender.sendMessage(Component.text("We found a MavenLoaderAPI updater!", NamedTextColor.BLUE), source);
                    MessageSender.sendMessage(Component.text("This machine Mavenloader version ", NamedTextColor.YELLOW)
                                    .append(Component.text(version, NamedTextColor.AQUA))
                                    .append(Component.text(", available updated version ", NamedTextColor.YELLOW))
                                    .append(Component.text(result.newVersion(), NamedTextColor.AQUA))
                            , source
                    );
                    MessageSender.sendMessage(Component.text("Update information: ", NamedTextColor.YELLOW), source);
                    MessageSender.sendMessage(Component.text(result.updateInfo()), source);
                    MessageSender.sendMessage(Component.text("Download and updater here: ", NamedTextColor.YELLOW)
                            .append(Component.text(result.updateUrl(), NamedTextColor.AQUA)), source);
                } else {
                    MessageSender.sendMessage(Component.text("MavenloaderAPI is already the latest version!", NamedTextColor.GREEN), server.getConsole());
                }
            }
        });
    }
}
