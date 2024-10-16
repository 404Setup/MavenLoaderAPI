package one.tranic.mavenloader.common.updater.spiget.source;

import java.util.Date;
import java.util.UUID;

public class SpigetLatestVersionSource {
    private int downloads;
    private String name;
    private Rating rating;
    private int releaseDate;
    private int resource;
    private String uuid;
    private int id;

    public SpigetLatestVersionSource() {
    }

    public int getDownloads() {
        return downloads;
    }

    public String getName() {
        return name;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public Date getReleaseDateOf() {
        return new Date(releaseDate);
    }

    public int getResource() {
        return resource;
    }

    public String getUuid() {
        return uuid;
    }

    public UUID getUuidOf() {
        return UUID.fromString(uuid);
    }

    public int getId() {
        return id;
    }

    public Rating getRating() {
        return rating;
    }

    public static class Rating {
        private int count;
        private int average;

        public Rating() {
        }
    }
}
