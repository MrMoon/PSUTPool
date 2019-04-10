package com.psut.pool.Models;

public class Car {
    //Global Variables and Objects:
    private String type, model, color;
    private Driver driver;

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public Driver getDriver() {
        return driver;
    }

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

}
