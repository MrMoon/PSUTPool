package com.psut.pool.Models;

import android.os.Build;

import com.psut.pool.Shared.Constants;

import java.util.HashMap;
import java.util.Map;

public class Driver extends User {
    //Global Variables and Objects:
    private String carID;
    private Car car;

    public Driver() {

    }

    public Driver(String status) {
        super(status);
    }

    public Driver(String name, String email, String uniID, String phoneNumber, String address, String preferred, String gender, String isDriver, String status, String carID) {
        super(name, email, uniID, phoneNumber, address, preferred, gender, isDriver, status);
        this.carID = carID;
    }

    public Driver(String name, String email, String uniID, String phoneNumber, String address, String preferred, String gender, String isDriver, String status, String curruntLatitude, String curruntLongitude, String carID, Car car) {
        super(name, email, uniID, phoneNumber, address, preferred, gender, isDriver, status, curruntLatitude, curruntLongitude);
        this.carID = carID;
        this.car = car;
    }

    public Driver(String curruntLatitude, String curruntLongitude) {
        super(curruntLatitude, curruntLongitude);
    }

    public Map<String, Object> toDriverMap() {
        return toUserObjectMap(Constants.DATABASE_DRIVER_CAR_ID, carID);
    }

    public Map<String, Object> toDriverCarMap() {
        HashMap<String, Object> driverCars = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            driverCars.forEach(toUserObjectMap("Car ID", carID)::putIfAbsent);
            driverCars.forEach(car.toCarMap()::putIfAbsent);
        }
        return driverCars;
    }
}
