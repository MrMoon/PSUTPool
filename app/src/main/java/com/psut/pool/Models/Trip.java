package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

public class Trip {
    //Global Variables and Objects:
    private String id, tripRank;
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

    public Trip(String id, String tripRank, User user, Ride ride) {
        this.id = id;
        this.tripRank = tripRank;
        this.user = user;
        this.ride = ride;
    }

    public Map<String, Object> toTripMap() {
        HashMap<String, Object> trips = new HashMap<>();
        trips.put("ID", id);
        trips.put("Trip Ranlk", tripRank);
        return trips;
    }

    public Map<String, Object> toFullTripMap() {
        HashMap<String, Object> fullTrip = new HashMap<>();
        fullTrip.putAll(toTripMap());
        fullTrip.putAll(ride.toFullRideMap());
        fullTrip.putAll(user.toUserMap());
        return fullTrip;
    }
}
