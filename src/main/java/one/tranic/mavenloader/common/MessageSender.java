package one.tranic.mavenloader.common;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import one.tranic.mavenloader.Platform;
import org.jetbrains.annotations.Nullable;

public class MessageSender {
    private static AudienceProvider adventure;
    private static Object plugin;

    public static @Nullable AudienceProvider adventure() {
        if (adventure == null) {
            adventure = switch (Platform.get()) {
                case Velocity, Paper, ShreddedPaper, Folia -> null;
                case BungeeCord -> BungeeAudiences.create((Plugin) plugin);
                case Spigot -> BukkitAudiences.create((org.bukkit.plugin.Plugin) plugin);
            };
        }
        return adventure;
    }

    public static @Nullable BungeeAudiences bungeeAdventure() {
        return (BungeeAudiences) adventure();
    }

    /**
     * This method should not be used by the platform from Paper, it is just created to compatible with Spigot.
     */
    public static @Nullable BukkitAudiences bukkitAdventure() {
        return (BukkitAudiences) adventure();
    }

    public static void setPlugin(Object plugin) {
        MessageSender.plugin = plugin;
        switch (Platform.get()) {
            case BungeeCord, Spigot: {
                if (adventure == null)
                    adventure();
            }
        }
    }

    public static void sendMessage(Component message, Object sender) {
        switch (Platform.get()) {
            case BungeeCord -> MessageSender.bungeeAdventure().sender((CommandSender) sender)
                    .sendMessage(message);
            case Spigot ->
                    MessageSender.bukkitAdventure().sender((org.bukkit.command.CommandSender) sender).sendMessage(message);
            case Paper, Folia, ShreddedPaper ->
                    ((org.bukkit.command.CommandSender) sender).sendMessage(LegacyComponentSerializer.legacySection().serialize(message));
            case Velocity -> ((CommandSource) sender).sendMessage(message);
        }
    }

    public static void close() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }
}
