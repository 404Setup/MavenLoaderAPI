package one.tranic.mavenloader.common.updater.spigot;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.tranic.mavenloader.common.updater.UpdateRecord;
import one.tranic.mavenloader.common.updater.Updater;

import java.io.IOException;
import java.util.Objects;

public class SpigotUpdate implements Updater {
    private final String localVersion;
    private final int resourceId;
    private final OkHttpClient client = new OkHttpClient();

    public SpigotUpdate(String localVersion, int resourceId) {
        this.localVersion = localVersion;
        this.resourceId = resourceId;
    }

    @Override
    public UpdateRecord getUpdate() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) return empty;
            String body = response.body().string();

            if (!response.isSuccessful()) {
                if (response.code() == 404 && body.equals("Invalid resource")) {
                    throw new IOException("Invalid resource");
                } else throw new IOException("Unexpected code " + response);
            }

            if (!Objects.equals(localVersion, body)) {
                return new UpdateRecord(true, body, "Update info is empty", "https://www.spigotmc.org/resources/" + resourceId + "/");
            }
        }

        return null;
    }
}
