package com.psut.pool.Models;

import com.psut.pool.Shared.Constants;

import java.util.HashMap;
import java.util.Map;

public class Car {
    //Global Variables and Objects:
    private String type, model, color;
    private Driver driver;

    public Car() {

    }

    public Car(String type, String model, String color) {
        this.type = type;
        this.model = model;
        this.color = color;
    }

    public Car(String type, String model, String color, Driver driver) {
        this.type = type;
        this.model = model;
        this.color = color;
        this.driver = driver;
    }

    Map<String, Object> toCarMap() {
        HashMap<String, Object> cars = new HashMap<>();
        cars.put(Constants.DATABASE_CAR_TYPE, type);
        cars.put(Constants.DATABASE_CAR_MODEL, model);
        cars.put(Constants.DATABASE_CAR_COLOR, color);
        return cars;
    }

}
