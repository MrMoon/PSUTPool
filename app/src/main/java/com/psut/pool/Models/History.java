package com.psut.pool.Models;

import android.os.Build;

import java.util.HashMap;
import java.util.Map;

public class History {
    //Global Variables and Objects:
    private String date,time;
    private Trip trip;

    public History(String date, String time, Trip trip) {
        this.date = date;
        this.time = time;
        this.trip = trip;
    }

    public Map<String,Object>toHistoryMap(){
        HashMap<String,Object>Histories=new HashMap<>();
        Histories.put("Date",date);
        Histories.put("Time",time);
        return Histories;
    }

    public Map<String, Object> toFullHistoryMap() {
        HashMap<String, Object> fullHistory = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fullHistory.forEach(toHistoryMap()::putIfAbsent);
            fullHistory.forEach(trip.toTripMap()::putIfAbsent);
        }
        return fullHistory;
    }
}
