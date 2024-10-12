package one.tranic.mavenloader;

public enum Platform {
    Velocity, BungeeCord, Spigot, Paper, ShreddedPaper, Folia;

    private static Platform platform;

    public static Platform get() {
        // They can only be checked using the reflection method like this.
        // To reduce excessive overhead, the result is permanently cached after performing a check once.
        if (platform != null) {
            return platform;
        }

        // test Velocity
        try {
            Class.forName("com.velocitypowered.api.proxy.Player");
            platform = Velocity;
            return platform;
        } catch (ClassNotFoundException e) {
            // no velocity
        }


        // now test BungeeCord (or Waterfall?)
        try {
            Class.forName("net.md_5.bungee.api.CommandSender");
            platform = BungeeCord;
            return platform;
        } catch (ClassNotFoundException e) {
            // no bungee or another fork
        }

        // now test folia
        try {
            org.bukkit.Bukkit.class.getMethod("getRegionScheduler");
            // Maybe Folia, but we won't be checking for more forks.
            platform = Folia;
            return platform;
        } catch (NoSuchMethodException e) {
            // no folia or folia fork
        }

        // now test shreddedpaper
        try {
            Class.forName("io.multipaper.shreddedpaper.threading.ShreddedPaperTickThread");
            platform = ShreddedPaper;
            return platform;
        } catch (ClassNotFoundException e) {
            // no shreddedpaper or shreddedpaper fork
        }

        // now maybe paper
        try {
            Class.forName("io.papermc.paper.util.MCUtil");
            platform = Paper;
            return platform;
        } catch (ClassNotFoundException e) {
            // Oh... maybe spigot and its forks?
        }

        platform = Spigot;
        return Spigot;
    }

    public static Platform of(String name) {
        return switch (name.toLowerCase()) {
            case "velocity" -> Velocity;
            case "bungeecord" -> BungeeCord;
            case "spigot" -> Spigot;
            case "paper" -> Paper;
            case "shreddedpaper" -> ShreddedPaper;
            case "folia" -> Folia;
            default -> throw new IllegalArgumentException("Unknown platform: " + name);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case Velocity -> "velocity";
            case BungeeCord -> "bungeecord";
            case Spigot -> "spigot";
            case Paper -> "paper";
            case ShreddedPaper -> "shreddedpaper";
            case Folia -> "folia";
        };
    }
}
