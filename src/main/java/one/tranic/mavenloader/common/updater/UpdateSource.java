package one.tranic.mavenloader.common.updater;

import org.jetbrains.annotations.NotNull;

public enum UpdateSource {
    Spigot,
    Spiget,
    Github,
    Hangar,
    Modrinth;

    public static UpdateSource of(@NotNull String value) {
        return switch (value.toLowerCase()) {
            case "spigot" -> Spigot;
            case "spiget" -> Spiget;
            case "github" -> Github;
            case "hangar" -> Hangar;
            case "modrinth" -> Modrinth;
            default -> throw new IllegalArgumentException("Unknown updater source: " + value);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case Spigot -> "spigot";
            case Spiget -> "spiget";
            case Github -> "github";
            case Hangar -> "hangar";
            case Modrinth -> "modrinth";
        };
    }
}
