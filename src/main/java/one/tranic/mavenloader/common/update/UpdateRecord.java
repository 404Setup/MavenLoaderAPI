package one.tranic.mavenloader.common.update;

public record UpdateRecord(boolean hasUpdate, String newVersion, String updateInfo, String updateUrl) {
}
