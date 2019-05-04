package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

import static com.psut.pool.Shared.Constants.DATABASE_AMOUNT;
import static com.psut.pool.Shared.Constants.DATABASE_DATE;
import static com.psut.pool.Shared.Constants.DATABASE_DESCRIPTION;

public class Ride {

    //Global Variables and Objects:
    private String description, amount, date;
    private TripRoute tripRoute;

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }

    public TripRoute getTripRoute() {
        return tripRoute;
    }

    public Ride(String description, String amount, String date, TripRoute tripRoute) {
        this.description = description;
        this.amount = amount;
        this.tripRoute = tripRoute;
        this.date = date;
    }

    private Map<String, Object> toRideMap() {
        HashMap<String, Object> rides = new HashMap<>();
        rides.put(DATABASE_DESCRIPTION, description);
        rides.put(DATABASE_AMOUNT, amount);
        rides.put(DATABASE_DATE, date);
        return rides;
    }

    Map<String, Object> toFullRideMap() {
        HashMap<String, Object> fullRide = new HashMap<>();
        fullRide.putAll(toRideMap());
        fullRide.putAll(tripRoute.toRouteMap());
        return fullRide;
    }
}