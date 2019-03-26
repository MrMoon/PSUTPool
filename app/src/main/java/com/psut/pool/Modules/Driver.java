package com.psut.pool.Modules;

public class Driver extends Users {
    //Global Variables and Objects:
    private String carID, address;

    public Driver(String name, String id, String phoneNumber, String isVerified, String carID, String address) {
        super(name, id, phoneNumber, isVerified);
        this.carID = carID;
        this.address = address;
    }
}
