package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

import static com.psut.pool.Shared.Constants.DATABASE_NUMBER;
import static com.psut.pool.Shared.Constants.DATABASE_RATING;

public class Rating {
    String rating, numberOfPepole;

    public Rating(String rating, String numberOfPepole) {
        this.rating = rating;
        this.numberOfPepole = numberOfPepole;
    }

    public Map<String, Object> toRatingMap() {
        HashMap<String, Object> ratings = new HashMap<>();
        ratings.put(DATABASE_RATING, rating);
        ratings.put(DATABASE_NUMBER, numberOfPepole);
        return ratings;
    }

    public String getRate() {
        return String.valueOf(Float.valueOf(rating) / Float.valueOf(numberOfPepole));
    }
}
