package one.tranic.mavenloader.common.updater.modrinth;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.common.updater.UpdateRecord;
import one.tranic.mavenloader.common.updater.Updater;
import one.tranic.mavenloader.common.updater.VersionComparator;
import one.tranic.mavenloader.common.updater.modrinth.source.Loaders;
import one.tranic.mavenloader.common.updater.modrinth.source.ModrinthVersionSource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class ModrinthUpdate implements Updater {
    private @NotNull
    final String slug;
    private @NotNull
    final String localVersion;
    private @NotNull
    final String loader;
    private @NotNull
    final String gameVersion;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();


    /**
     * Spigot:
     * <pre>
     *     new ModrinthUpdate("mavenloader-api", getDescription().getVersion(), Loaders.of("spigot"), getServer().getVersion());
     * </pre>
     *
     * @param slug         the Project id or project slug, not be {@code null}.
     * @param localVersion the local version of project , not be {@code null}.
     * @param loader       the Loader Name, not be {@code null}.
     * @param gameVersion  the gameVersion, not be {@code null}.
     */
    public ModrinthUpdate(@NotNull String slug, @NotNull String localVersion, @NotNull Loaders loader, @NotNull String gameVersion) {
        this(slug, localVersion, loader.toString(), gameVersion);
    }

    /**
     * Spigot:
     * <pre>
     *     new ModrinthUpdate("mavenloader-api", getDescription().getVersion(), Loaders.of("spigot"), getServer().getVersion());
     * </pre>
     *
     * @param slug         the Project id or project slug, not be {@code null}.
     * @param localVersion the local version of project , not be {@code null}.
     * @param loader       the Loader Name, not be {@code null}.
     * @param gameVersion  the gameVersion, not be {@code null}.
     */
    public ModrinthUpdate(@NotNull String slug, @NotNull String localVersion, @NotNull String loader, @NotNull String gameVersion) {
        this.slug = slug;
        this.localVersion = localVersion;
        this.loader = loader;
        this.gameVersion = gameVersion;
    }

    @Override
    public UpdateRecord getUpdate() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url("https://api.modrinth.com/v2/project/" + slug + "/version")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) return empty;

            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    throw new IOException("Invalid resource");
                } else throw new IOException("Unexpected code " + response);
            }

            ModrinthVersionSource[] updater = gson.fromJson(response.body().string(), ModrinthVersionSource[].class);
            if (updater.length == 0) return empty;

            for (ModrinthVersionSource source : updater) {
                if (!source.getGameVersions().contains(gameVersion)) return empty;
                if (!source.getLoaders().contains(loader)) return empty;
                if (Config.isUpdaterSimpleMode()) {
                    if (!Objects.equals(source.getVersionNumber(), localVersion)) {
                        return new UpdateRecord(true, source.getVersionNumber(), source.getChangelog(), "https://modrinth.com/plugin/" + source.getProjectId() + "/version/" + source.getId());
                    }
                } else if (VersionComparator.cmpVer(localVersion, source.getVersionNumber()) < 0) {
                    return new UpdateRecord(true, source.getVersionNumber(), source.getChangelog(), "https://modrinth.com/plugin/" + source.getProjectId() + "/version/" + source.getId());
                }
            }
        }

        return empty;
    }
}
