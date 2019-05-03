package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

import static com.psut.pool.Shared.Constants.DATABASE_DISTANCE;
import static com.psut.pool.Shared.Constants.DATABASE_DROP_OFF_LOCATION;
import static com.psut.pool.Shared.Constants.DATABASE_DURATION;
import static com.psut.pool.Shared.Constants.DATABASE_PICK_UP_LOCATION;
import static com.psut.pool.Shared.Constants.DATABASE_TRIP_STATUS;

public class TripRoute {

    //Global Variables and Objects:
    private String pickUpLocation, dropOffLocation, distance, duration, status;

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public String getDropOffLocation() {
        return dropOffLocation;
    }

    public String getDistance() {
        return distance;
    }

    public String getStatus() {
        return status;
    }

    public TripRoute(String pickUpLocation, String dropOffLocation, String distance, String duration, String status) {
        this.pickUpLocation = pickUpLocation;
        this.dropOffLocation = dropOffLocation;
        this.distance = distance;
        this.status = status;
        this.duration = duration;
    }

    public Map<String, Object> toRouteMap() {
        HashMap<String, Object> routes = new HashMap<>();
        routes.put(DATABASE_PICK_UP_LOCATION, pickUpLocation);
        routes.put(DATABASE_DROP_OFF_LOCATION, dropOffLocation);
        routes.put(DATABASE_DISTANCE, distance);
        routes.put(DATABASE_DURATION, duration);
        routes.put(DATABASE_TRIP_STATUS, status);
        return routes;
    }
}
