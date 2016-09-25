package ru.zennex.zennextesttask.parse;

/**
 * Created by Kostez on 23.09.2016.
 */

public class Quote {

    private final int id;
    private final String description;
    private final String time;
    private final int rating;

    public Quote(int id, String description, String time, int rating) {
        this.id = id;
        this.description = description;
        this.time = time;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public int getRating() {
        return rating;
    }
}
