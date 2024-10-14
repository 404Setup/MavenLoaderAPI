package one.tranic.mavenloader.common.update;

import java.io.IOException;

public interface Updater {
    UpdateRecord getUpdate() throws IOException;
}
