package one.tranic.mavenloader.common.update;

import org.jetbrains.annotations.NotNull;

public record UpdateRecord(boolean hasUpdate, @NotNull String newVersion, @NotNull String updateInfo, @NotNull String updateUrl) {
}
