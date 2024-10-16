package one.tranic.mavenloader.common.update.spiget.source;

import java.util.Date;

public class SpigetLatestUpdateSource {
    private int date;
    private String description;
    private int likes;
    private int resource;
    private String title;
    private int id;

    public SpigetLatestUpdateSource() {
    }

    public int getDate() {
        return date;
    }

    public Date getDateOf() {
        return new Date(date);
    }

    public int getLikes() {
        return likes;
    }

    public int getResource() {
        return resource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}
