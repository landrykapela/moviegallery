package tz.co.neelansoft.cinegallery.library;

public class Trailer {
    private String id;
    private String key;
    private String name;

    public Trailer(String id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

}
