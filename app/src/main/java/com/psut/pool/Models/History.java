package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

public class History {
    //Global Variables and Objects:
    private String date,time;

    public History(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public Map<String,Object>toHistoryMap(){
        HashMap<String,Object>Histories=new HashMap<>();
        Histories.put("Date",date);
        Histories.put("Time",time);
        return Histories;
    }
}
