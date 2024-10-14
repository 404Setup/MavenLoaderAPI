package one.tranic.mavenloader.update;

import java.io.IOException;

public interface Updater {
    UpdateRecord getUpdate() throws IOException;
}
