package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

import static com.psut.pool.Shared.Constants.DATABASE_DRIVER_NAME;
import static com.psut.pool.Shared.Constants.DATABASE_TRIP_RANK;
import static com.psut.pool.Shared.Constants.DRIVER_ID;

public class Trip {
    //Global Variables and Objects:
    private String id, tripRank, driverName;
    private User user;
    private Ride ride;

    public String getId() {
        return id;
    }

    public String getTripRank() {
        return tripRank;
    }

    public User getUser() {
        return user;
    }

    public Ride getRide() {
        return ride;
    }

    public Trip(String id, String tripRank, User user, Ride ride, String driverName) {
        this.id = id;
        this.tripRank = tripRank;
        this.user = user;
        this.ride = ride;
        this.driverName = driverName;
    }

    public Map<String, Object> toTripMap() {
        HashMap<String, Object> trips = new HashMap<>();
        trips.put(DRIVER_ID, id);
        trips.put(DATABASE_TRIP_RANK, tripRank);
        return trips;
    }

    public Map<String, Object> toFullTripMap() {
        HashMap<String, Object> fullTrip = new HashMap<>();
        fullTrip.putAll(toTripMap());
        fullTrip.putAll(ride.toFullRideMap());
        fullTrip.put(DATABASE_DRIVER_NAME, driverName);
        return fullTrip;
    }
}
