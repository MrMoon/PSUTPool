package com.psut.pool.Models;

import android.os.Build;

import java.util.HashMap;
import java.util.Map;

public class Trip {
    //Global Variables and Objects:
    private String id, tripRank;
    private User user;
    private Ride ride;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fullTrip.forEach(toTripMap()::putIfAbsent);
            fullTrip.forEach(ride.toFullRideMap()::putIfAbsent);
            fullTrip.forEach(user.toUserMap()::putIfAbsent);
        }
        return fullTrip;
    }
}
