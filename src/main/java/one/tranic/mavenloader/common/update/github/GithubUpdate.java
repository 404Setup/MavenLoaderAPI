package one.tranic.mavenloader.common.update.github;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.tranic.mavenloader.common.update.UpdateRecord;
import one.tranic.mavenloader.common.update.Updater;

import java.io.IOException;
import java.util.Objects;

public class GithubUpdate implements Updater {
    private final String myVersion;
    private final String repo;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public GithubUpdate(String myVersion, String repo) {
        this.myVersion = myVersion;
        this.repo = repo;
    }

    @Override
    public UpdateRecord getUpdate() throws IOException {
        Request request = new Request.Builder()
                .get()
                .header("Accept", "application/json")
                .url("https://api.github.com/repos/" + repo + "/releases/latest")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            if (response.body() == null) return empty;

            GithubRelease updater = gson.fromJson(response.body().string(), GithubRelease.class);
            if (!Objects.equals(myVersion, updater.getTagName())) {
                return new UpdateRecord(true, updater.getTagName(), updater.getBody().replaceAll("\\s*\\*\\*Full Changelog\\*\\*.*", ""), "https://github.com/"+repo+"/releases/tag/" + updater.getTagName());
            }
        }
        return empty;
    }
}
