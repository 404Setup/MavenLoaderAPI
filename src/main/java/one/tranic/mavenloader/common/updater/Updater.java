package one.tranic.mavenloader.common.updater;

import java.io.IOException;

public interface Updater {
    UpdateRecord empty = new UpdateRecord(false, "", "", "");

    UpdateRecord getUpdate() throws IOException;
}
