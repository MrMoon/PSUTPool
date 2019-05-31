package com.psut.pool.Models;

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

    public Driver(String name, String email, String uniID, String phoneNumber, String address, String preferred, String gender, String isDriver, String status, String carID, Car car) {
        super(name, email, uniID, phoneNumber, address, preferred, gender, isDriver, status);
        this.carID = carID;
        this.car = car;
    }

    public Driver(String curruntLatitude, String curruntLongitude) {
        super(curruntLatitude, curruntLongitude);
    }



    public Map<String, Object> toDriverMap() {
        return toUserObjectMap(Constants.DATABASE_DRIVER_CAR_ID, carID);
    }

    public Map<String, Object> toFullDriverMap() {
        HashMap<String, Object> driverCars = new HashMap<>();
        driverCars.putAll(toUserObjectMap(Constants.DATABASE_DRIVER_CAR_ID, carID));
        driverCars.putAll(toCarMap());
        return driverCars;
    }

    public Map<String, Object> toCarMap() {
        HashMap<String, Object> cars = new HashMap<>();
        cars.put(Constants.DATABASE_CAR_TYPE, car.getType());
        cars.put(Constants.DATABASE_CAR_MODEL, car.getModel());
        cars.put(Constants.DATABASE_CAR_COLOR, car.getColor());
        return cars;
    }

    public Map<String, Object> toDriverLocations() {
        //        driverLocations.put("ID", id.toString());
        return new HashMap<>(toUserLocationMap());
    }
}
