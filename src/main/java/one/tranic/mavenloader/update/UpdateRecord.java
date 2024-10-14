package one.tranic.mavenloader.update;

public record UpdateRecord(boolean hasUpdate, String newVersion, String updateInfo, String updateUrl) {
}
