package com.psut.pool.Models;

import android.os.Build;

import java.util.HashMap;
import java.util.Map;

public class Ride {

    //Global Variables and Objects:
    private String description, amount;
    private Route route;

    public Ride(String description, String amount, Route route) {
        this.description = description;
        this.amount = amount;
        this.route = route;
    }

    public Map<String, Object> toRideMap() {
        HashMap<String, Object> rides = new HashMap<>();
        rides.put("Discription", description);
        rides.put("Amount", amount);
        return rides;
    }

    public Map<String, Object> toFullRideMap() {
        HashMap<String, Object> fullRide = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fullRide.forEach(toRideMap()::putIfAbsent);
            fullRide.forEach(route.toRouteMap()::putIfAbsent);
        }
        return fullRide;
    }
}