package one.tranic.mavenloader.bungee;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.common.MessageSender;
import one.tranic.mavenloader.common.update.UpdateRecord;
import one.tranic.mavenloader.common.update.UpdateSource;
import one.tranic.mavenloader.common.update.Updater;
import one.tranic.mavenloader.common.update.github.GithubUpdate;
import one.tranic.mavenloader.common.update.spigot.SpigotUpdate;
import one.tranic.mavenloader.velocity.BuildConstants;

import java.io.IOException;

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
            case Spigot -> new SpigotUpdate(version, "119660");
            default -> throw new RuntimeException("This update channel: "+Config.getUpdaterSource()+" is not supported");
        };
        try {
            UpdateRecord result = updater.getUpdate();
            if (result != null) {
                if (result.hasUpdate()) {
                    CommandSender source = server.getConsole();
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
                    MessageSender.sendMessage(Component.text("MavenloaderAPI is already the latest version!", NamedTextColor.GREEN), server.getConsole());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
