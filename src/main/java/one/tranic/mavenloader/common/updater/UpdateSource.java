package one.tranic.mavenloader.common.updater;

import org.jetbrains.annotations.NotNull;

/**
 * Enum representing various update sources for plugins or software.
 * <p>
 * Supported update sources include:
 * <ul>
 *     <li>{@link #Spigot}</li>
 *     <li>{@link #Spiget}</li>
 *     <li>{@link #Github}</li>
 *     <li>{@link #Hangar}</li>
 *     <li>{@link #Modrinth}</li>
 * </ul>
 */
public enum UpdateSource {
    /**
     * Represents the Spigot platform as an update source.
     * Spigot is a popular platform for Minecraft server plugins.
     */
    Spigot,

    /**
     * Represents the Spiget API as an update source.
     * Spiget is an API providing access to Spigot's resources programmatically.
     */
    Spiget,

    /**
     * Represents GitHub as an update source.
     * GitHub is a widely used platform for hosting open-source projects and releases.
     */
    Github,

    /**
     * Represents Hangar as an update source.
     * Hangar is a platform for hosting plugins, similar to Spigot and Modrinth.
     */
    Hangar,

    /**
     * Represents Modrinth as an update source.
     * Modrinth is a popular platform for Minecraft mods and plugins.
     */
    Modrinth;

    /**
     * Converts a string value to the corresponding {@link UpdateSource} enum constant.
     * <p>
     * This method compares the provided value (case-insensitive) to known update source names
     * and returns the appropriate enum constant.
     * </p>
     *
     * @param value the name of the update source (e.g., "spigot", "github")
     * @return the corresponding {@link UpdateSource} enum constant
     * @throws IllegalArgumentException if the update source name is unknown
     */
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

    /**
     * Returns the lowercase string representation of the update source.
     *
     * @return the update source name in lowercase (e.g., "spigot", "github").
     */
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
