package one.tranic.mavenloader.update.github;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.tranic.mavenloader.update.UpdateRecord;
import one.tranic.mavenloader.update.Updater;

import java.io.IOException;
import java.util.Objects;

public class GithubUpdate implements Updater {
    private final String myVersion;
    private final OkHttpClient client = new OkHttpClient();
    private final UpdateRecord empty = new UpdateRecord(false, null, null, null);
    private final Gson gson = new Gson();

    public GithubUpdate(String myVersion) {
        this.myVersion = myVersion;
    }

    @Override
    public UpdateRecord getUpdate() throws IOException {
        Request request = new Request.Builder()
                .get()
                .header("Accept", "application/json")
                .url("https://api.github.com/repos/LevelTranic/MavenLoader/releases/latest")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            GithubRelease updater = gson.fromJson(response.body().string(), GithubRelease.class);
            if (!Objects.equals(myVersion, updater.getTagName())) {
                return new UpdateRecord(true, updater.getTagName(), updater.getBody().replaceAll("\\s*\\*\\*Full Changelog\\*\\*.*", ""), "https://github.com/LevelTranic/MavenLoader/releases/download/" + updater.getTagName() + "/MavenLoader-" + updater.getTagName() + ".jar");
            }
        }
        return empty;
    }
}
