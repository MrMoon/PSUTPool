package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

abstract public class User {
    private String uniID, name, phoneNumber, gender, preferred, isDriver, address;

    public User() {

    }

    User(String uniID, String name, String phoneNumber, String gender, String preferred, String isDriver) {
        this.uniID = uniID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.preferred = preferred;
        this.isDriver = isDriver;
    }

    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUniID() {
        return uniID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getPreferred() {
        return preferred;
    }

    public String getIsDriver() {
        return isDriver;
    }

    public String getAddress() {
        return address;
    }

    Map<String, Object> toUserMap(String s0, String s1) {
        HashMap<String, Object> users = new HashMap<>();
        users.put("Uni ID", uniID);
        users.put("Name", name);
        users.put("Phone Number", phoneNumber);
        users.put("Gender", gender);
        users.put("Preferred", preferred);
        users.put("Driver", isDriver);
        users.put(s0, s1);
        return users;
    }
}
