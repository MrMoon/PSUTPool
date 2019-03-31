package com.psut.pool.Models;

import java.util.Map;

public class Driver extends User {
    private String carID;

    public Driver(String uniID, String name, String phoneNumber, String gender, String preferred, String isDriver, String carID) {
        super(uniID, name, phoneNumber, gender, preferred, isDriver);
        this.carID = carID;
    }

    public Map<String, Object> toDriverMap() {
        return toUserMap("Car ID", carID);
    }
}
