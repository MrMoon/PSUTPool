package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

import static com.psut.pool.Shared.Constants.DATABASE_DRIVER_NAME;
import static com.psut.pool.Shared.Constants.DRIVER_ID;

public class Trip {
    //Global Variables and Objects:
    private String id, driverName;
    private User user;
    private Ride ride;
    private Rating rating;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Ride getRide() {
        return ride;
    }

    public Trip(String id, User user, Ride ride, String driverName, Rating rating) {
        this.id = id;
        this.user = user;
        this.ride = ride;
        this.driverName = driverName;
        this.rating = rating;
    }

    public Map<String, Object> toTripMap() {
        HashMap<String, Object> trips = new HashMap<>();
        trips.put(DRIVER_ID, id);
        return trips;
    }

    public Map<String, Object> toFullTripMap() {
        HashMap<String, Object> fullTrip = new HashMap<>();
        fullTrip.putAll(toTripMap());
        fullTrip.putAll(ride.toFullRideMap());
        fullTrip.putAll(rating.toRatingMap());
        fullTrip.put(DATABASE_DRIVER_NAME, driverName);
        return fullTrip;
    }
}
