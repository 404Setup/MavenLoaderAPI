package one.tranic.mavenloader.common.update.spigot;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.tranic.mavenloader.common.update.UpdateRecord;
import one.tranic.mavenloader.common.update.Updater;

import java.io.IOException;
import java.util.Objects;

public class SpigotUpdate implements Updater {
    private final String localVersion;
    private final String resourceID;
    private final OkHttpClient client = new OkHttpClient();

    public SpigotUpdate(String localVersion, String resourceID) {
        this.localVersion = localVersion;
        this.resourceID = resourceID;
    }

    @Override
    public UpdateRecord getUpdate() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID)
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
                return new UpdateRecord(true, body, "Update info is empty", "https://www.spigotmc.org/resources/" + resourceID + "/");
            }
        }

        return null;
    }
}
