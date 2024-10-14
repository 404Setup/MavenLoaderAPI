package one.tranic.mavenloader.common.loader;

import org.jetbrains.annotations.Nullable;

public class VelocityPlugin {
    private String id;
    private String name;
    private String version;
    private String description;
    private String url;
    private String main;
    private String[] authors;

    public VelocityPlugin() {}

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getVersion() {
        return version;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getMain() {
        return main;
    }

    @Nullable
    public Class<?> getMainClass() {
        try {
            return Class.forName(main);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public String[] getAuthors() {
        return authors;
    }
}
