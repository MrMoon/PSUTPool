package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

public class Trip {
    //Global Variables and Objects:
    private String id, tripRank;
    private User user;

    public Trip(String id, String tripRank, User user) {
        this.id = id;
        this.tripRank = tripRank;
        this.user = user;
    }

    public Map<String, Object> toTripMap(Map<String, Object> map) {
        HashMap<String, Object> trips = new HashMap<>();
        trips.put("ID", id);

        return trips;
    }
}
