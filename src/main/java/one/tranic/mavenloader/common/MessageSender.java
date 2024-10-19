package one.tranic.mavenloader.common;

import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import one.tranic.mavenloader.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageSender {
    private static AudienceProvider adventure;
    private static Object plugin;

    private static @NotNull AudienceProvider adventure() {
        if (adventure == null) {
            adventure = switch (Platform.get()) {
                case Velocity, Paper, ShreddedPaper, Folia ->
                        throw new RuntimeException(Platform.get() + " has native Kyori API compatibility");
                case BungeeCord -> BungeeAudiences.create((net.md_5.bungee.api.plugin.Plugin) plugin);
                case Spigot -> throw new RuntimeException("MavenLoaderAPI no longer provides Spigot compatibility, it brings too much trouble.");
                //case Spigot -> BukkitAudiences.create((org.bukkit.plugin.Plugin) plugin);
            };
        }
        return adventure;
    }

    private static @NotNull BungeeAudiences bungeeAdventure() {
        return (BungeeAudiences) adventure();
    }

    public static void setPlugin(@NotNull Object plugin) {
        if (MessageSender.plugin == null) {
            if (!plugin.getClass().getPackage().getName().startsWith("one.tranic.mavenloader"))
                throw new RuntimeException(plugin.getClass().getCanonicalName() + " is not a MavenLoaderAPI class");
            MessageSender.plugin = plugin;
        }
        switch (Platform.get()) {
            case BungeeCord, Spigot: {
                if (adventure == null)
                    adventure();
            }
        }
    }

    public static void sendMessage(@Nullable String message, @NotNull Object sender) {
        if (message == null) return;
        sendMessage(Component.text(message), sender);
    }

    public static void sendMessage(@Nullable Component message, @NotNull Object sender) {
        if (message == null) return;
        switch (Platform.get()) {
            case BungeeCord -> MessageSender.bungeeAdventure().sender((net.md_5.bungee.api.CommandSender) sender)
                    .sendMessage(message);
            /*case Spigot ->
                    MessageSender.bukkitAdventure().sender((org.bukkit.command.CommandSender) sender).sendMessage(message);*/
            case Paper, Folia, ShreddedPaper ->
                    ((org.bukkit.command.CommandSender) sender).sendMessage(message);
            case Velocity -> ((com.velocitypowered.api.command.CommandSource) sender).sendMessage(message);
        }
    }

    public static void sendMessage(@Nullable ComponentLike message, Object sender) {
        if (message == null) return;
        sendMessage(message.asComponent(), sender);
    }

    public static void close() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }
}
