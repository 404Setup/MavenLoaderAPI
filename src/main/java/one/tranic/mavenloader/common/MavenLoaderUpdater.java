package one.tranic.mavenloader.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.common.updater.UpdateSource;
import one.tranic.mavenloader.common.updater.Updater;
import one.tranic.mavenloader.common.updater.github.GithubUpdate;
import one.tranic.mavenloader.common.updater.hangar.HangarUpdate;
import one.tranic.mavenloader.common.updater.modrinth.ModrinthUpdate;
import one.tranic.mavenloader.common.updater.modrinth.source.Loaders;
import one.tranic.mavenloader.common.updater.spiget.SpigetUpdate;
import one.tranic.mavenloader.common.updater.spigot.SpigotUpdate;

public class MavenLoaderUpdater {
    private final Updater updater;
    private final String localVersion;
    private final Object sender;

    public MavenLoaderUpdater(final String localVersion, final Object sender, final Loaders loader) {
        if (!Config.isUpdaterCheck()) {
            this.localVersion = null;
            this.sender = null;
            this.updater = null;
            return;
        }

        this.localVersion = localVersion;
        this.sender = sender;
        this.updater = switch (UpdateSource.of(Config.getUpdaterSource())) {
            case Github -> new GithubUpdate(localVersion, "LevelTranic/MavenLoader");
            case Spigot -> new SpigotUpdate(localVersion, 119660);
            case Spiget -> new SpigetUpdate(localVersion, 119660);
            case Modrinth -> new ModrinthUpdate("mavenloader-api", localVersion, loader, "1.21.1"); // Hardcoded to the highest currently compatible game version.
            case Hangar ->
                    new HangarUpdate(localVersion, "mavenloaderapi", "https://hangar.papermc.io/Tranic/MavenLoaderAPI");
            default ->
                    throw new RuntimeException("This updater channel: " + Config.getUpdaterSource() + " is not supported");
        };
    }

    public void checkUpdate() {
        if (!Config.isUpdaterCheck()) return;
        updater.getUpdateAsync((result) -> {
            if (result != null) {
                if (result.hasUpdate()) {
                    MessageSender.sendMessage(Component.text("We found a MavenLoaderAPI updater!", NamedTextColor.BLUE), sender);
                    MessageSender.sendMessage(Component.text("This machine Mavenloader version ", NamedTextColor.YELLOW)
                                    .append(Component.text(localVersion, NamedTextColor.AQUA))
                                    .append(Component.text(", available updated version ", NamedTextColor.YELLOW))
                                    .append(Component.text(result.newVersion(), NamedTextColor.AQUA))
                            , sender
                    );
                    MessageSender.sendMessage(Component.text("Update information: ", NamedTextColor.YELLOW), sender);
                    MessageSender.sendMessage(Component.text(result.updateInfo()), sender);
                    MessageSender.sendMessage(Component.text("Download and updater here: ", NamedTextColor.YELLOW)
                            .append(Component.text(result.updateUrl(), NamedTextColor.AQUA)), sender);
                } else {
                    MessageSender.sendMessage(Component.text("MavenloaderAPI is already the latest version!", NamedTextColor.GREEN), sender);
                }
            }
        });
    }
}
