package com.psut.pool.Models;

import java.util.Map;

public class Driver extends User {
    private String carID;

    public Driver() {

    }

    public Driver(String status) {
        super(status);
    }

    public Driver(String name, String email, String uniID, String phoneNumber, String address, String preferred, String gender, String isDriver, String status, String carID) {
        super(name, email, uniID, phoneNumber, address, preferred, gender, isDriver, status);
        this.carID = carID;
    }

    public Map<String, Object> toDriverMap() {
        return toUserMap("Car ID", carID);
    }
}
