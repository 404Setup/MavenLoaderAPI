package one.tranic.mavenloader.common.update;

import java.io.IOException;

public interface Updater {
    UpdateRecord empty = new UpdateRecord(false, "", "", "");

    UpdateRecord getUpdate() throws IOException;
}
