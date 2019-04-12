package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

public class Ride {

    //Global Variables and Objects:
    private String description, amount;
    private TripRoute tripRoute;

    public Ride(String description, String amount, TripRoute tripRoute) {
        this.description = description;
        this.amount = amount;
        this.tripRoute = tripRoute;
    }

    public Map<String, Object> toRideMap() {
        HashMap<String, Object> rides = new HashMap<>();
        rides.put("Discription", description);
        rides.put("Amount", amount);
        return rides;
    }

    public Map<String, Object> toFullRideMap() {
        HashMap<String, Object> fullRide = new HashMap<>();
        fullRide.putAll(toRideMap());
        fullRide.putAll(tripRoute.toRouteMap());
        return fullRide;
    }
}