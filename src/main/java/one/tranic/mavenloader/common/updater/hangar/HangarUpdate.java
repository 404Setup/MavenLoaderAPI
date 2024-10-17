package one.tranic.mavenloader.common.updater.hangar;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.tranic.mavenloader.Config;
import one.tranic.mavenloader.common.updater.UpdateRecord;
import one.tranic.mavenloader.common.updater.Updater;
import one.tranic.mavenloader.common.updater.VersionComparator;
import one.tranic.mavenloader.common.updater.hangar.source.CombinedResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class HangarUpdate implements Updater {
    private final String localVersion;
    private final String projectId;
    private final String address;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public HangarUpdate(String localVersion, String projectId, String address) {
        this.localVersion = localVersion;
        this.projectId = projectId;
        this.address = address;
    }

    @Override
    public UpdateRecord getUpdate() throws IOException {
        Request request = new Request.Builder()
                .get()
                .header("Accept", "application/json")
                .url("https://hangar.papermc.io/api/v1/projects/" + projectId + "/versions")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            if (response.body() == null) return empty;

            CombinedResponse updater = gson.fromJson(response.body().string(), CombinedResponse.class);
            List<CombinedResponse.VersionResult> result = updater.getResult();
            if (result.isEmpty()) return empty;

            CombinedResponse.VersionResult first = result.get(0);

            if (Config.isUpdaterSimpleMode()) {
                if (!Objects.equals(localVersion, first.getName())) {
                    return new UpdateRecord(true, first.getName(), first.getDescription(), address + "/versions/" + first.getName());
                }
            } else if (VersionComparator.cmpVer(localVersion, first.getName()) < 0) {
                return new UpdateRecord(true, first.getName(), first.getDescription(), address + "/versions/" + first.getName());
            }
        }
        return empty;
    }
}
