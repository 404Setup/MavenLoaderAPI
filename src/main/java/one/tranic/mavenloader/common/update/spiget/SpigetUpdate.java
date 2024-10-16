package one.tranic.mavenloader.common.update.spiget;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.tranic.mavenloader.common.Utils;
import one.tranic.mavenloader.common.update.UpdateRecord;
import one.tranic.mavenloader.common.update.Updater;
import one.tranic.mavenloader.common.update.spiget.source.SpigetLatestUpdateSource;
import one.tranic.mavenloader.common.update.spiget.source.SpigetLatestVersionSource;

import java.io.IOException;
import java.util.Objects;

public class SpigetUpdate implements Updater {
    private final String localVersion;
    private final int resourceId;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public SpigetUpdate(String localVersion, int resourceId) {
        this.localVersion = localVersion;
        this.resourceId = resourceId;
    }

    @Override
    public UpdateRecord getUpdate() throws IOException {
        Request request = new Request.Builder()
                .get()
                .header("Accept", "application/json")
                .url("https://api.spiget.org/v2/resources/" + resourceId + "/versions/latest")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            if (response.body() == null) return empty;

            SpigetLatestVersionSource updater = gson.fromJson(response.body().string(), SpigetLatestVersionSource.class);
            if (!Objects.equals(localVersion, updater.getName())) {
                Request request2 = new Request.Builder()
                        .get()
                        .header("Accept", "application/json")
                        .url("https://api.spiget.org/v2/resources/" + resourceId + "/updates/latest")
                        .build();

                try (Response response2 = client.newCall(request2).execute()) {
                    if (!response2.isSuccessful()) throw new IOException("Unexpected code " + response2);
                    if (response2.body() == null) return empty;

                    SpigetLatestUpdateSource updater2 = gson.fromJson(response2.body().string(), SpigetLatestUpdateSource.class);
                    return new UpdateRecord(true, updater.getName(), Utils.decodeAndStripHtml(updater2.getDescription()), "https://www.spigotmc.org/resources/" + resourceId + "/");
                }
            }
        }
        return empty;
    }
}
